package com.github.prgrms.social.api.controller.user;

import com.github.prgrms.social.api.error.NotFoundException;
import com.github.prgrms.social.api.model.api.request.user.*;
import com.github.prgrms.social.api.model.api.response.ApiResult;
import com.github.prgrms.social.api.model.api.response.user.JoinResult;
import com.github.prgrms.social.api.model.user.*;
import com.github.prgrms.social.api.security.JWT;
import com.github.prgrms.social.api.security.JwtAuthentication;
import com.github.prgrms.social.api.service.user.EmailService;
import com.github.prgrms.social.api.service.user.UserService;
import com.github.prgrms.social.api.validator.CheckEmailValidator;
import com.github.prgrms.social.api.validator.CheckNameValidator;
import com.github.prgrms.social.api.validator.CheckProfileValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.github.prgrms.social.api.model.api.response.ApiResult.OK;
import static com.github.prgrms.social.api.model.commons.AttachedFile.toAttachedFile;

@Slf4j
@RestController
@RequestMapping("api")
@Api(tags = "사용자 APIs")
@RequiredArgsConstructor
public class UserRestController {

    private final JWT jwt;

    private final UserService userService;

    private final EmailService emailService;

    private final ReplyingKafkaTemplate<String, Subscription, Subscription> replyingKafkaTemplate;

    private final CheckEmailValidator checkEmailValidator;

    private final CheckNameValidator checkNameValidator;

    private final CheckProfileValidator checkProfileValidator;

    @Value("${spring.kafka.topic.request}")
    private String requestTopic;

    @Value("${spring.kafka.topic.response}")
    private String responseTopic;

    @InitBinder("checkEmailRequest")
    public void initEmailCheckBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(checkEmailValidator);
    }

    @InitBinder("checkNameRequest")
    public void initNameCheckBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(checkNameValidator);
    }

    @InitBinder("profileRequest")
    public void initProfileCheckBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(checkProfileValidator);
    }

    @PostMapping(path = "user/exists/email")
    @ApiOperation(value = "이메일 중복확인 (API 토큰 필요없음)")
    public ApiResult<Boolean> checkEmail(
            @ApiParam(value = "example: {\"address\": \"test00@gmail.com\"}")
            @Valid CheckEmailRequest checkEmailRequest,
            Errors errors
    ) {

        if(errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            throw new IllegalArgumentException(message == null ? "" : message);
        }

        return OK(true);
    }

    @PostMapping(path = "user/exists/name")
    @ApiOperation(value = "이름 중복확인 (API 토큰 필요없음)")
    public ApiResult<Boolean> checkName(
            @ApiParam(value = "example: {\"name\": \"test00\"}")
            @Valid CheckNameRequest checkNameRequest,
            Errors errors
    ) {

        if(errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            throw new IllegalArgumentException(message == null ? "" : message);
        }

        return OK(true);
    }

    @PostMapping(path = "user/join", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "회원가입 (API 토큰 필요없음)")
    public ApiResult<JoinResult> join(
            @RequestPart(required = false) MultipartFile file,
            @Valid JoinRequest joinRequest,
            Errors errors
    ) throws IOException {

        if(errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            throw new IllegalArgumentException(message == null ? "" : message);
        }

        User user = userService.join(
                joinRequest.getName(),
                new Email(joinRequest.getAddress()),
                joinRequest.getPassword(),
                toAttachedFile(file)
        );


        emailService.sendMessage(user);

        String apiToken = user.newApiToken(jwt, new String[]{Role.USER.getValue()});
        return OK(new JoinResult(apiToken, user));
    }

    @GetMapping(path = "user/resend-email")
    public ApiResult<Boolean> resendEmail(@AuthenticationPrincipal JwtAuthentication authentication) {
        User user = userService.findById(authentication.id.getValue())
                .orElseThrow(() -> new NotFoundException(User.class, authentication.id.getValue()));

        emailService.sendMessage(user);

        return OK(true);
    }

    // Subscribe 요청 처리.(카프카에게 Subscribe 정보 전송 후 응답 처리)
    @PostMapping(path = "subscribe")
    public ApiResult<Subscription> subscribe(
            @RequestBody SubscribeRequest subscribeRequest,
            @AuthenticationPrincipal JwtAuthentication authentication
    ) throws InterruptedException, ExecutionException, IOException {


        Subscription subscription = subscribeRequest.newSubscription(
                authentication.id.getValue(),
                subscribeRequest.getNotificationEndPoint(),
                subscribeRequest.getPublicKey(),
                subscribeRequest.getAuth()
        );

        ProducerRecord<String, Subscription> record = new ProducerRecord<>(requestTopic,subscription);
        record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, responseTopic.getBytes()));


        RequestReplyFuture<String, Subscription, Subscription> replyFuture = replyingKafkaTemplate.sendAndReceive(record);

        ConsumerRecord<String, Subscription> consumerRecord = replyFuture.get();

        log.info("success to subscribe {}",consumerRecord.value());

        return OK(consumerRecord.value());
    }

    @GetMapping(path = "user/me")
    @ApiOperation(value = "내 정보")
    public ApiResult<User> me(@AuthenticationPrincipal JwtAuthentication authentication) {
        return OK(
                userService.findById(authentication.id.getValue())
                        .orElseThrow(() -> new NotFoundException(User.class, authentication.id))
        );
    }

    @PutMapping(path = "user/profile")
    @ApiOperation(value = "유저 정보 수정")
    public ApiResult<User> updateProfile(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @Valid ProfileRequest profileRequest,
            Errors errors
    ) throws IOException {

        if(errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            throw new IllegalArgumentException(message == null ? "" : message);
        }

        return OK(userService.updateProfile(authentication.id.getValue(), profileRequest));

    }

    @PutMapping(path = "user/profile-image")
    @ApiOperation(value = "유저 정보 수정",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResult<User> updateProfileImage(
            @RequestPart MultipartFile file,
            @AuthenticationPrincipal JwtAuthentication authentication,
            MultipartHttpServletRequest request
    ) throws IOException {

        return OK(userService.updateProfileImage(authentication.id.getValue(), file, request.getServletContext().getRealPath("/")));

    }


    @GetMapping(path = "user/{id}")
    @ApiOperation(value = "다른 사람 정보")
    public ApiResult<User> findUser(@PathVariable Long id) {

        return OK(
                userService.findById(id)
                        .orElseThrow(() -> new NotFoundException(User.class, id))
        );
    }

    @GetMapping(path = "user/followings")
    @ApiOperation(value = "팔로우 목록")
    public ApiResult<List<User>> followings(
            @AuthenticationPrincipal JwtAuthentication authentication,
            Pageable pageable
    ) {
        return OK(userService.getFollowings(authentication.id.getValue(),pageable));
    }

    @GetMapping(path = "user/followers")
    @ApiOperation(value = "팔로우 목록")
    public ApiResult<List<User>> followers(
            @AuthenticationPrincipal JwtAuthentication authentication,
            Pageable pageable
    ) {
        return OK(userService.getFollowers(authentication.id.getValue(),pageable));
    }

    @PostMapping(path = "user/{userId}/follow")
    @ApiOperation(value = "팔로우")
    public ApiResult<User> follow(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @PathVariable Long userId
    ) {
        return OK(userService.addFollowing(authentication.id.getValue(), userId));
    }

    @DeleteMapping(path = "user/{userId}/follow")
    @ApiOperation(value = "팔로우")
    public ApiResult<Long> unfollow(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @PathVariable Long userId
    ) {
        return OK(userService.removeFollowing(authentication.id.getValue(),userId));
    }

    @DeleteMapping(path = "user/{userId}/follower")
    @ApiOperation(value = "팔로우")
    public ApiResult<Long> removeFollower(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @PathVariable Long userId
    ) {
        return OK(userService.removeFollower(authentication.id.getValue(), userId));
    }
}
