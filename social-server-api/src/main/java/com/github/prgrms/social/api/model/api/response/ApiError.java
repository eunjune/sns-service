package com.github.prgrms.social.api.model.api.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.http.HttpStatus;

// 에러 응답 VO
@Getter
@NoArgsConstructor(force = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@ToString
public class ApiError {

    @ApiModelProperty(value = "오류 메시지", required = true)
    private final String message;

    @ApiModelProperty(value = "HTTP 오류코드", required = true)
    private final int status;

    ApiError(Throwable throwable, HttpStatus status) {
        this(throwable.getMessage(), status.value());
    }
}