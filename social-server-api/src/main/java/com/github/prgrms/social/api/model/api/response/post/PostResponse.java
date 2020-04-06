package com.github.prgrms.social.api.model.api.response.post;

import com.github.prgrms.social.api.model.api.response.user.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {

    private Long id;

    private String content;

    private boolean likesOfMe;

    private boolean isRetweet;

    private LocalDateTime createAt;

    private Integer commentCount;

    private Integer likeCount;

    private Set<String> images = new HashSet<>();

    private UserResponse user;

    private PostResponse retweetPost;
}
