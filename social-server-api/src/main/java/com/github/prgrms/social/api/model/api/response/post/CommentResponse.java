package com.github.prgrms.social.api.model.api.response.post;

import com.github.prgrms.social.api.model.api.response.user.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {

    private Long id;

    private String content;

    private LocalDateTime createdAt;

    private UserResponse user;
}
