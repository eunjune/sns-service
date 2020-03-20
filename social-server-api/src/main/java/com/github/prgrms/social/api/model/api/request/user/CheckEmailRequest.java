package com.github.prgrms.social.api.model.api.request.user;

// 이메일 중복확인 요청 데이터를 받는 DTO

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CheckEmailRequest {

    @NotBlank(message = "이메일이 존재하지 않습니다.")
    @javax.validation.constraints.Email(message = "이메일 형식이 맞지 않습니다.")
    private String address;
}
