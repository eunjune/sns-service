package com.github.prgrms.social.api.model.api.response.user;

import com.github.prgrms.social.api.model.user.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 회원가입 완료 응답 VO

@Getter
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class JoinResponse {

    @ApiModelProperty(value = "API 토큰", required = true)
    private final String token;

    @ApiModelProperty(value = "사용자 정보", required = true)
    private final User user;

}
