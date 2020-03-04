package com.github.prgrms.social.api.security;

import com.github.prgrms.social.api.model.user.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import static com.google.common.base.Preconditions.checkNotNull;

// Authentication의 details를 위한 클래스
@Getter
public class AuthenticationResult {

    @ApiModelProperty(value = "API 토큰", required = true)
    private final String token;

    @ApiModelProperty(value = "사용자 정보", required = true)
    private final User user;

    public AuthenticationResult(String token, User user) {
        checkNotNull(token, "apiToken must be provided.");
        checkNotNull(user, "user must be provided.");

        this.token = token;
        this.user = user;
    }
}
