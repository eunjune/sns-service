package com.github.prgrms.social.api.model.api.request.user;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class CheckNameRequest {

    @NotBlank(message = "이름이 존재하지 않습니다.")
    @Length(min = 3, max = 20)
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9_-]{3,20}$", message = "이름은 3~20자 입니다.(특수기호 제외)")
    private String name;
}
