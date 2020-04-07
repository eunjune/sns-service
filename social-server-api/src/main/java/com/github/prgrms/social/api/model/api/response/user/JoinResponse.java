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

    private final String token;

    private final User user;

}
