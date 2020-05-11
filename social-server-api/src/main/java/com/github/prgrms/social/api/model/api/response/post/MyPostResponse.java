package com.github.prgrms.social.api.model.api.response.post;

import com.github.prgrms.social.api.model.api.response.user.UserResponse;
import com.github.prgrms.social.api.model.post.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyPostResponse {

    private Long id;

    private String content;

    private boolean likesOfMe;

    private LocalDateTime createdAt;

    private Integer likeCount;

    private Integer retweetCount;

    private Set<CommentResponse> comments = new HashSet<>();

    private Set<String> images = new HashSet<>();

    private UserResponse user;
}
