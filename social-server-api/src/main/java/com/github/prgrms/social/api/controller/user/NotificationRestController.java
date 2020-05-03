package com.github.prgrms.social.api.controller.user;

import com.github.prgrms.social.api.model.api.response.ApiResult;
import com.github.prgrms.social.api.model.api.response.user.NotificationResponse;
import com.github.prgrms.social.api.security.JwtAuthentication;
import com.github.prgrms.social.api.service.user.NotificationService;
import com.github.prgrms.social.api.util.DtoUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.github.prgrms.social.api.model.api.response.ApiResult.OK;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
@Api(tags = "알림 APIs")
public class NotificationRestController {

    private final NotificationService notificationService;

    private final DtoUtils dtoUtils;

    @GetMapping("user/notification/new")
    @ApiOperation(value = "새로운 알림 데이터 가져오기")
    public ApiResult<List<NotificationResponse>> newNotification(@AuthenticationPrincipal JwtAuthentication authentication) {
        return OK(notificationService.getNewNotification(authentication.id.getValue())
                .stream()
                .map(dtoUtils::convertNotificationResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping("user/notification/read")
    @ApiOperation(value = "읽은 알림 데이터 가져오기")
    public ApiResult<List<NotificationResponse>> readedNotification(@AuthenticationPrincipal JwtAuthentication authentication) {
        return OK(notificationService.getReadNotification(authentication.id.getValue())
                .stream()
                .map(dtoUtils::convertNotificationResponse)
                .collect(Collectors.toList()));
    }


    @PatchMapping("user/notification/{id}")
    @ApiOperation(value = "알림 읽음")
    public ApiResult<NotificationResponse> readCheck(@AuthenticationPrincipal JwtAuthentication authentication,
                                                     @ApiParam(value = "알림 PK", example = "1", required = true) @PathVariable Long id) {
        return OK(dtoUtils.convertNotificationResponse(notificationService.readCheck(id)));
    }

    @DeleteMapping("user/notification/{id}")
    @ApiOperation(value = "알림 삭제")
    public ApiResult<Long> deleteNotification(@AuthenticationPrincipal JwtAuthentication authentication,
                                              @ApiParam(value = "알림 PK", example = "1", required = true) @PathVariable Long id) {
        return OK(notificationService.deleteNotification(id));
    }

}
