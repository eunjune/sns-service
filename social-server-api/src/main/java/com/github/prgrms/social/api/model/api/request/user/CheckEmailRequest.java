package com.github.prgrms.social.api.model.api.request.user;

// 이메일 중복확인 요청 데이터를 받는 DTO

import lombok.Getter;

@Getter
public class CheckEmailRequest {
    private String address;
}
