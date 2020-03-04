package com.github.prgrms.social.api.security;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 로그인 요청의 인증 정보를 담는 DTO
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class AuthenticationRequest {

    @ApiModelProperty(value = "로그인 이메일", required = true)
    private String address;

    @ApiModelProperty(value = "로그인 비밀번호", required = true)
    private String password;
}
