package com.github.prgrms.social.api.controller.user;

import com.github.prgrms.social.api.error.NotFoundException;
import com.github.prgrms.social.api.model.api.request.user.CheckEmailRequest;
import com.github.prgrms.social.api.model.api.request.user.JoinRequest;
import com.github.prgrms.social.api.model.api.request.user.SubscribeRequest;
import com.github.prgrms.social.api.model.api.response.ApiResult;
import com.github.prgrms.social.api.model.api.response.user.JoinResult;
import com.github.prgrms.social.api.model.user.*;
import com.github.prgrms.social.api.security.JWT;
import com.github.prgrms.social.api.security.JwtAuthentication;
import com.github.prgrms.social.api.service.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.github.prgrms.social.api.model.api.response.ApiResult.OK;
import static com.github.prgrms.social.api.model.commons.AttachedFile.toAttachedFile;

@Slf4j
@RestController
@RequestMapping("api")
@Api(tags = "사용자 APIs")
public class UserRestController {

    private final JWT jwt;

    private final UserService userService;

    private final ReplyingKafkaTemplate<String, Subscription, Subscription> replyingKafkaTemplate;

    @Value("${spring.kafka.topic.request}")
    private String requestTopic;

    @Value("${spring.kafka.topic.response}")
    private String responseTopic;


    public UserRestController(JWT jwt, UserService userService, ReplyingKafkaTemplate<String, Subscription, Subscription> replyingKafkaTemplate) {
        this.jwt = jwt;
        this.userService = userService;
        this.replyingKafkaTemplate = replyingKafkaTemplate;
    }

    @PostMapping(path = "user/exists")
    @ApiOperation(value = "이메일 중복확인 (API 토큰 필요없음)")
    public ApiResult<Boolean> checkEmail(
            @ApiParam(value = "example: {\"address\": \"test00@gmail.com\"}")
            @RequestBody CheckEmailRequest checkEmailRequest
    ) {
        return OK(userService.findByEmail(checkEmailRequest.emailOf()).isPresent());
    }

    @PostMapping(path = "user/join", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "회원가입 (API 토큰 필요없음)")
    public ApiResult<JoinResult> join(
            @ModelAttribute JoinRequest joinRequest,
            @RequestPart(required = false) MultipartFile file
    ) throws IOException {
        User user = userService.join(
                joinRequest.getName(),
                new Email(joinRequest.getPrincipal()),
                joinRequest.getCredentials(),
                toAttachedFile(file)
        );
        String apiToken = user.newApiToken(jwt, new String[]{Role.USER.getValue()});
        return OK(new JoinResult(apiToken, user));
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

    @GetMapping(path = "user/connections")
    @ApiOperation(value = "내 친구 목록")
    public ApiResult<List<ConnectedUser>> connections(@AuthenticationPrincipal JwtAuthentication authentication) {
        return OK(userService.findAllConnectedUser(authentication.id.getValue()));
    }
}
