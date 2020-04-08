package com.github.prgrms.social.api.model.api.response.user;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class MeResponse {

    private Long id;

    private String email;

    private String name;

    private String profileImageUrl;

    private int loginCount;

    private boolean isEmailCertification;

    private boolean isPrivate;

    private LocalDateTime createAt;

    private LocalDateTime lastLoginAt;

    private Set<Long> followings;

    private Set<Long> followers;

    private Integer postCount;
}
