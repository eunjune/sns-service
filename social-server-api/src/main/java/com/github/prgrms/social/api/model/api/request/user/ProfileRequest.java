package com.github.prgrms.social.api.model.api.request.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequest {

    @ApiModelProperty(value = "사용자 이름", required = true)
    private String name;

    @ApiModelProperty(value = "회원가입 비밀번호", required = true)
    private String password;

    private boolean isPrivate;
}
