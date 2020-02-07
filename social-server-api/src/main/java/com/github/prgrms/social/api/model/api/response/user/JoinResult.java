package com.github.prgrms.social.api.model.api.response.user;

import com.github.prgrms.social.api.model.user.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.google.common.base.Preconditions.checkNotNull;

// 회원가입 완료 응답 VO

@Getter
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class JoinResult {

    @ApiModelProperty(value = "API 토큰", required = true)
    private final String apiToken;

    @ApiModelProperty(value = "사용자 정보", required = true)
    private final User user;
/*

    public JoinResult(String apiToken, User user) {
        checkNotNull(apiToken, "apiToken must be provided.");
        checkNotNull(user, "user must be provided.");

        this.apiToken = apiToken;
        this.user = user;
    }

    public String getApiToken() {
        return apiToken;
    }

    public User getUser() {
        return user;
    }
*/

}
