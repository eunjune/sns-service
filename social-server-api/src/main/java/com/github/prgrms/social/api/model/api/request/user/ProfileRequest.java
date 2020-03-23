package com.github.prgrms.social.api.model.api.request.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;

@Data
public class ProfileRequest {

    @ApiModelProperty(value = "사용자 이름", required = true)
    @Length(min = 3, max = 20)
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9_-]{3,20}$")
    private String name;

    @ApiModelProperty(value = "회원가입 비밀번호", required = true)
    @Length(min = 8,max = 50)
    private String password;

    private String profileImageUrl;
}
