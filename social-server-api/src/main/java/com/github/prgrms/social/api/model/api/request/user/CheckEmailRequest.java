package com.github.prgrms.social.api.model.api.request.user;

// 이메일 중복확인 요청 데이터를 받는 DTO

import com.github.prgrms.social.api.model.user.Email;
import lombok.Getter;

import static com.github.prgrms.social.api.model.user.Email.checkAddress;

@Getter
public class CheckEmailRequest {
    private String address;

    public Email emailOf() {
        if(!checkAddress(address)) {
            throw new IllegalArgumentException("이메일 형식이 맞지 않습니다.");
        }

        return new Email(address);
    }
}
