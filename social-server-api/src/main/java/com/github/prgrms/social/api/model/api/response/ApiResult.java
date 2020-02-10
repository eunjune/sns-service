package com.github.prgrms.social.api.model.api.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

// 응답 VO
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@ToString
public class ApiResult<T> {

    @ApiModelProperty(value = "API 요청 처리 결과", required = true)
    private final boolean success;

    @ApiModelProperty(value = "success가 true라면, API 요청 처리 응답값")
    private final T response;

    @ApiModelProperty(value = "success가 false라면, API 요청 처리 응답값")
    private final ApiError error;

    public static <T> ApiResult<T> OK(T response) {
        return new ApiResult<>(true, response, null);
    }

    public static ApiResult ERROR(Throwable throwable, HttpStatus status) {
        return new ApiResult<>(false, null, new ApiError(throwable, status));
    }

    public static ApiResult ERROR(String errorMessage, HttpStatus status) {
        return new ApiResult<>(false, null, new ApiError(errorMessage, status.value()));
    }
}