package com.github.prgrms.social.api.model.api.request.post;

import com.github.prgrms.social.api.model.post.Comment;
import lombok.*;

// 댓글작성 요청 데이터를 받는 DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {

    private String content;

    public Comment newComment() {
        return Comment.builder()
                .content(content)
                .build();

    }
}
