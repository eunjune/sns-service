package com.github.prgrms.social.api.repository.post;

import com.github.prgrms.social.api.model.post.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface JpaCommentRepository extends JpaRepository<Comment,Long> {

    Comment save(Comment comment);

    @Transactional(readOnly = true)
    Optional<Comment> findBySeq(Long commentSeq);

    /*
    특정 포스트의 댓글들*/
    @Transactional(readOnly = true)
    List<Comment> findByPost_SeqOrderBySeqDesc(Long seq); // post의 id
}
