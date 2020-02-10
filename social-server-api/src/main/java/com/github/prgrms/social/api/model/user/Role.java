package com.github.prgrms.social.api.model.user;

// 인가 권한 enum

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public enum Role {

    USER("ROLE_USER");

    private String value;
}
