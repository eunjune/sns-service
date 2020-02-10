package com.github.prgrms.social.api.model.api.request.post;

import com.github.prgrms.social.api.model.post.Comment;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

// 댓글작성 요청 데이터를 받는 DTO
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class CommentRequest {

    private String contents;

    public Comment newComment() {
        return Comment.builder()
                .contents(contents)
                .build();

    }
}
