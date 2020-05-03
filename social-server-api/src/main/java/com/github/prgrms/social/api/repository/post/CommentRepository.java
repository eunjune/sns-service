package com.github.prgrms.social.api.repository.post;

import com.github.prgrms.social.api.model.post.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    Comment save(Comment comment);

    @Transactional(readOnly = true)
    Optional<Comment> findById(Long id);

    @Transactional(readOnly = true)
    List<Comment> findByPost_IdOrderByIdDesc(Long postId);
}
