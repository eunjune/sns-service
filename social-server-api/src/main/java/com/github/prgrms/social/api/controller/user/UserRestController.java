package com.github.prgrms.social.api.controller.user;

import com.github.prgrms.social.api.error.NotFoundException;
import com.github.prgrms.social.api.model.api.request.user.*;
import com.github.prgrms.social.api.model.api.response.ApiResult;
import com.github.prgrms.social.api.model.api.response.user.JoinResponse;
import com.github.prgrms.social.api.model.api.response.user.MeResponse;
import com.github.prgrms.social.api.model.api.response.user.UserResponse;
import com.github.prgrms.social.api.model.user.Email;
import com.github.prgrms.social.api.model.user.Role;
import com.github.prgrms.social.api.model.user.Subscription;
import com.github.prgrms.social.api.model.user.User;
import com.github.prgrms.social.api.security.JWT;
import com.github.prgrms.social.api.security.JwtAuthentication;
import com.github.prgrms.social.api.service.user.EmailService;
import com.github.prgrms.social.api.service.user.UserService;
import com.github.prgrms.social.api.util.DtoUtils;
import com.github.prgrms.social.api.validator.CheckEmailValidator;
import com.github.prgrms.social.api.validator.CheckNameValidator;
import io.swagger.annotations.*;
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
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static com.github.prgrms.social.api.model.api.response.ApiResult.OK;

@Slf4j
@RestController
@RequestMapping("api")
@Api(tags = "사용자 APIs")
@RequiredArgsConstructor
public class UserRestController {

    private final DtoUtils dtoUtils;

    private final JWT jwt;

    private final UserService userService;

    private final EmailService emailService;

    private final ReplyingKafkaTemplate<String, Subscription, Subscription> replyingKafkaTemplate;

    private final CheckEmailValidator checkEmailValidator;

    private final CheckNameValidator checkNameValidator;

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

    @GetMapping(path = "user/resend-email")
    @ApiOperation(value = "회원가입 인증 메일 재전송")
    public ApiResult<Boolean> resendEmail(@AuthenticationPrincipal JwtAuthentication authentication) {
        User user = userService.getUser(authentication.id.getValue())
                .orElseThrow(() -> new NotFoundException(User.class, authentication.id.getValue()));

        emailService.sendEmailCertificationMessage(user);

        return OK(true);
    }

    @GetMapping(path = "user/me")
    @ApiOperation(value = "내 정보")
    public ApiResult<MeResponse> me(@AuthenticationPrincipal JwtAuthentication authentication) {
        return OK(
                dtoUtils.convertMeResponse(
                        userService.getUserWithConnectedUserAndPost(authentication.id.getValue())
                                .orElseThrow(() -> new NotFoundException(User.class, authentication.id))
                )
        );
    }

    // TODO: 팔로우, 게시글 데이터 확인
    @GetMapping(path = "user/{id}")
    @ApiOperation(value = "다른 사람 정보")
    public ApiResult<UserResponse> findUser(
            @ApiParam(value = "유저 PK", example = "1", required = true, type = "integer") @PathVariable("id") User user
    ) {

        return OK(dtoUtils.convertUserResponse(user));
    }

    // TODO : LONG으로 바꿀 수 있음
    @GetMapping(path = "user/followings")
    @ApiOperation(value = "팔로잉 리스트")
    public ApiResult<List<UserResponse>> followings(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @ApiIgnore Pageable pageable
    ) {

        return OK(userService.getFollowings(authentication.id.getValue(), pageable)
                            .stream()
                            .map(dtoUtils::convertUserResponse)
                            .collect(Collectors.toList()));
    }

    // TODO : LONG으로 바꿀 수 있음
    @GetMapping(path = "user/followers")
    @ApiOperation(value = "팔로워 리스트")
    public ApiResult<List<UserResponse>> followers(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @ApiIgnore Pageable pageable
    ) {
        return OK(userService.getFollowers(authentication.id.getValue(),pageable)
                            .stream()
                            .map(dtoUtils::convertUserResponse)
                            .collect(Collectors.toList()));
    }

    @PostMapping(path = "user/exists/email")
    @ApiOperation(value = "이메일 중복확인 (API 토큰 필요없음)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "address", dataType = "string", paramType = "form", defaultValue = "", value = "이메일",required = true),
    })
    public ApiResult<Boolean> checkEmail(
            @ApiParam(value = "example: {\"address\": \"test00@gmail.com\"}") @Valid CheckEmailRequest checkEmailRequest,
            @ApiIgnore Errors errors
    ) {

        if(errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            throw new IllegalArgumentException(message == null ? "" : message);
        }

        return OK(true);
    }

    @PostMapping(path = "user/exists/name")
    @ApiOperation(value = "이름 중복확인 (API 토큰 필요없음)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", dataType = "string", paramType = "form", defaultValue = "", value = "이름",required = true),
    })
    public ApiResult<Boolean> checkName(
            @Valid CheckNameRequest checkNameRequest,
            @ApiIgnore Errors errors
    ) {

        if(errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            throw new IllegalArgumentException(message == null ? "" : message);
        }

        return OK(true);
    }

    @PostMapping(path = "user/join", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "회원가입 (API 토큰 필요없음)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", dataType = "string", paramType = "form", defaultValue = "", value = "이름",required = true),
            @ApiImplicitParam(name = "address", dataType = "string", paramType = "form", defaultValue = "", value = "이메일",required = true),
            @ApiImplicitParam(name = "password", dataType = "string", paramType = "form", defaultValue = "", value = "비밀번호",required = true),
            @ApiImplicitParam(name = "profileImageUrl", dataType = "string", paramType = "form", defaultValue = "", value = "프로필 이미지 URL"),
    })
    public ApiResult<JoinResponse> join(
            @Valid JoinRequest joinRequest,
            @ApiIgnore Errors errors
    ) throws IOException {

        if(errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            throw new IllegalArgumentException(message == null ? "" : message);
        }

        User user = userService.join(
                joinRequest.getName(),
                new Email(joinRequest.getAddress()),
                joinRequest.getPassword()
        );

        emailService.sendEmailCertificationMessage(user);

        String apiToken = user.newApiToken(jwt, new String[]{Role.USER.getValue()});

        return OK(new JoinResponse(apiToken, user));
    }

    @PostMapping("check-email-token")
    @ApiOperation(value = "회원가입 이메일 인증")
    public ApiResult<MeResponse> checkEmailToken(
            @RequestBody EmailAuthenticationRequest emailAuthenticationRequest
    ) {
        return OK(dtoUtils.convertMeResponse(userService.certificateEmail(emailAuthenticationRequest.getEmailToken(), emailAuthenticationRequest.getEmail())));
    }

    @PostMapping(path = "user/follow/{userId}")
    @ApiOperation(value = "팔로우")
    public ApiResult<MeResponse> follow(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @ApiParam(value = "유저 PK", example = "1", required = true) @PathVariable Long userId
    ) {

        return OK(dtoUtils.convertMeResponse(userService.addFollowing(authentication.id.getValue(), userId)));
    }

    @PutMapping(path = "user/profile")
    @ApiOperation(value = "프로필 수정")
    public ApiResult<MeResponse> updateProfile(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @Valid @RequestBody ProfileRequest profileRequest,
            @ApiIgnore Errors errors
    ) throws IOException {

        if(errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            throw new IllegalArgumentException(message == null ? "" : message);
        }

        return OK(dtoUtils.convertMeResponse(userService.updateProfile(authentication.id.getValue(), profileRequest)));

    }

    @PutMapping(path = "user/profile-image")
    @ApiOperation(value = "프로필 이미지 수정",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResult<MeResponse> updateProfileImage(
            @RequestPart MultipartFile file,
            @AuthenticationPrincipal JwtAuthentication authentication,
            @ApiIgnore MultipartHttpServletRequest request
    ) throws IOException {

        return OK(dtoUtils.convertMeResponse(userService.updateProfileImage(authentication.id.getValue(), file, request.getServletContext().getRealPath("/"))));

    }

    @DeleteMapping(path = "user/follow/{userId}")
    @ApiOperation(value = "언팔로우")
    public ApiResult<Long> unfollow(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @ApiParam(value = "유저 PK", example = "1", required = true) @PathVariable Long userId
    ) {
        return OK(userService.removeFollowing(authentication.id.getValue(),userId));
    }

    @DeleteMapping(path = "user/follower/{userId}")
    @ApiOperation(value = "팔로워 삭제")
    public ApiResult<Long> removeFollower(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @ApiParam(value = "유저 PK", example = "1", required = true) @PathVariable Long userId
    ) {
        return OK(userService.removeFollower(authentication.id.getValue(), userId));
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
}
