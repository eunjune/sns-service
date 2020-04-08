package com.github.prgrms.social.api.security;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 로그인 요청의 인증 정보를 담는 DTO
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class AuthenticationRequest {

    private String address;

    private String password;
}
