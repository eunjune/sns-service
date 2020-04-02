package com.github.prgrms.social.api.model.api.response.user;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponse {

    private Long id;

    private String email;

    private String name;

    private String profileImageUrl;

    private LocalDateTime createAt;

    private LocalDateTime lastLoginAt;

}
