package com.github.prgrms.social.api.model.api.request.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

// 회원가입 요청 데이터를 받는 DTO

@Data
public class JoinRequest {

    @NotBlank
    @Length(min = 3, max = 20)
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9_-]{3,20}$")
    private String name;

    @Email(message = "이메일 형식이 맞지 않습니다.")
    private String address;

    // Todo 비밀번호 패턴 설정
    @NotBlank
    @Length(min = 8,max = 50, message = "비밀번호는 8자 이상")
    private String password;

    private String profileImageUrl;

}
