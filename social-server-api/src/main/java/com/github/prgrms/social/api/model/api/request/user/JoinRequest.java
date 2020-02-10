package com.github.prgrms.social.api.model.api.request.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// 회원가입 요청 데이터를 받는 DTO

@Getter
@Setter
@ToString
public class JoinRequest {

    @ApiModelProperty(value = "사용자 이름", required = true)
    private String name;

    @ApiModelProperty(value = "로그인 이메일", required = true)
    private String principal;

    @ApiModelProperty(value = "로그인 비밀번호", required = true)
    private String credentials;
}
