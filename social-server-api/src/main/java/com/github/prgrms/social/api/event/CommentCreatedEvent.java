package com.github.prgrms.social.api.event;

import com.github.prgrms.social.api.model.post.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static com.google.common.base.Preconditions.checkNotNull;

// 카프카에 전송하기 위한 댓글작성 이벤트 객체
@NoArgsConstructor(force = true)
@Getter
@ToString
public class CommentCreatedEvent {

    private final String commentWriter;

    private final Long postWriterSeq;

    public CommentCreatedEvent(Comment comment, Long postWriterSeq) {
        checkNotNull(comment.getUser(),"Comment User must be provided.");

        this.commentWriter = comment.getUser().getName();
        this.postWriterSeq = postWriterSeq;
    }
}
