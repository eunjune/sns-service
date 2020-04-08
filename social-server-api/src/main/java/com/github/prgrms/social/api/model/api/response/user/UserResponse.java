package com.github.prgrms.social.api.model.api.response.user;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponse {

    private Long id;

    private String name;

    private String profileImageUrl;

    private boolean isPrivate;

    private LocalDateTime createAt;

    private LocalDateTime lastLoginAt;

    private Integer followingCount;

    private Integer followerCount;

    private Integer postCount;
}
