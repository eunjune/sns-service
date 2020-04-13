package com.github.prgrms.social.api.service.post;

import com.github.prgrms.social.api.error.NotFoundException;
import com.github.prgrms.social.api.event.CommentCreatedEvent;
import com.github.prgrms.social.api.model.post.Comment;
import com.github.prgrms.social.api.model.post.Post;
import com.github.prgrms.social.api.model.user.User;
import com.github.prgrms.social.api.repository.post.CommentRepository;
import com.github.prgrms.social.api.repository.post.PostRepository;
import com.github.prgrms.social.api.repository.user.UserRepository;
import com.google.common.eventbus.EventBus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final UserRepository userRepository;

    private final PostRepository postRepository;

    private final CommentRepository commentRepository;

    private final EventBus eventBus;

    // TODO : postWriterId 삭제해도 무방할듯
    @Transactional(readOnly = true)
    public List<Comment> findAll(Long postId, Long userId, Long postWriterId) {
        return commentRepository.findByPost_IdAndUser_IdOrderByIdDesc(postId, userId);
    }

    @Transactional
    public Comment write(Long postId, Long userId, Long postWriterId, Comment comment) {
        checkNotNull(comment, "comment must be provided.");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(User.class,userId));

        return postRepository.findWithCommentByIdAndUser_Id(postId, postWriterId)
            .map(post -> {
                post.addComment(comment);
                comment.setUser(user);
                Comment saveComment = commentRepository.save(comment);
                eventBus.post(new CommentCreatedEvent(saveComment, postWriterId));
                return saveComment;
            }).orElseThrow(() -> new NotFoundException(Post.class, postId, userId));
    }


}
