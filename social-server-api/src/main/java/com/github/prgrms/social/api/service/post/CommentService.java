package com.github.prgrms.social.api.service.post;

import com.github.prgrms.social.api.error.NotFoundException;
import com.github.prgrms.social.api.event.CommentCreatedEvent;
import com.github.prgrms.social.api.model.post.Comment;
import com.github.prgrms.social.api.model.post.Post;
import com.github.prgrms.social.api.repository.post.JpaCommentRepository;
import com.github.prgrms.social.api.repository.post.JpaPostRepository;
import com.google.common.eventbus.EventBus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Collections.emptyList;

@Service
public class CommentService {

    private final JpaPostRepository postRepository;

    private final JpaCommentRepository commentRepository;

    private final EventBus eventBus;

    public CommentService(JpaPostRepository postRepository, JpaCommentRepository commentRepository, EventBus eventBus) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.eventBus = eventBus;
    }

    @Transactional
    public Comment write(Long postId, Long userId, Long postWriterId, Comment comment) {
        checkNotNull(comment, "comment must be provided.");

        return postRepository.findByIdCustom(postId, userId, postWriterId).map(post -> {
            post.getUser().addComment(comment);
            post.incrementAndGetComments(comment);
            postRepository.save(post);

            Comment saveComment = commentRepository.save(comment);
            eventBus.post(new CommentCreatedEvent(saveComment, postWriterId));
            return saveComment;
        }).orElseThrow(() -> new NotFoundException(Post.class, postId, userId));
    }

    @Transactional(readOnly = true)
    public List<Comment> findAll(Long postId, Long userId, Long postWriterId) {
        return postRepository.findByIdCustom(postId, userId, postWriterId)
                .map(post -> commentRepository.findByPost_IdOrderByIdDesc(postId))
                .orElse(emptyList());
    }
}
