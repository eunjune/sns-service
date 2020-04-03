package com.github.prgrms.social.api.model.api.response.user;

import com.github.prgrms.social.api.model.post.Post;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
public class MeResponse {

    private Long id;

    private String email;

    private String name;

    private String profileImageUrl;

    private long postCount;

    private int loginCount;

    private boolean isEmailCertification;

    private LocalDateTime createAt;

    private LocalDateTime lastLoginAt;

    private Set<Long> followings = new HashSet<>();

    private Set<Long> followers = new HashSet<>();

    private Set<Post> posts = new HashSet<>();
}
