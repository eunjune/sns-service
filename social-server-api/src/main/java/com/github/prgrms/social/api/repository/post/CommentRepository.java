package com.github.prgrms.social.api.repository.post;

import com.github.prgrms.social.api.model.commons.Id;
import com.github.prgrms.social.api.model.post.Comment;
import com.github.prgrms.social.api.model.post.Post;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {

    Comment save(Comment comment);

    void update(Comment comment);

    Optional<Comment> findById(Long seq);

    List<Comment> findAll(Id<Post, Long> postId);

}
