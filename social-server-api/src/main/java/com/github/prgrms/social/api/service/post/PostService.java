package com.github.prgrms.social.api.service.post;

import com.github.prgrms.social.api.error.NotFoundException;
import com.github.prgrms.social.api.model.post.Post;
import com.github.prgrms.social.api.model.user.Likes;
import com.github.prgrms.social.api.model.user.User;
import com.github.prgrms.social.api.repository.post.JpaPostLikeRepository;
import com.github.prgrms.social.api.repository.post.JpaPostRepository;
import com.github.prgrms.social.api.repository.user.JpaUserRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
public class PostService {

    private final JpaUserRepository userRepository;

    private final JpaPostRepository postRepository;

    private final JpaPostLikeRepository postLikeRepository;

    public PostService(JpaUserRepository userRepository, JpaPostRepository postRepository, JpaPostLikeRepository postLikeRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.postLikeRepository = postLikeRepository;
    }

    @Transactional
    public Post write(Post post, Long userSeq) {
        return userRepository.findBySeq(userSeq)
                .map(user -> {
                    user.addPost(post);
                    return postRepository.save(post);
                })
                .orElseThrow(() -> new NotFoundException(User.class, userSeq));
    }

    // 좋아요 기능 처리
    @Transactional
    public Optional<Post> like(Long postSeq, Long userSeq, Long postWriterSeq) {
        // TODO PostLikeRepository를 구현하고, 포스트 좋아요 서비스를 구현하세요.
        checkNotNull(postSeq, "postId must be provided.");
        checkNotNull(userSeq, "userId must be provided.");
        checkNotNull(postWriterSeq, "writerId must be provided.");

        return postRepository.findById(postSeq, userSeq, postWriterSeq)
            .map(post -> {
                if (!post.isLikesOfMe()) {
                    //post.incrementAndGetLikes();

                    Likes likes = new Likes(null,null);
                    post.getUser().addLike(likes);
                    post.incrementAndGetLikes(likes);
                    postLikeRepository.save(likes);
                }
            return post;
        });
    }

    @Transactional(readOnly = true)
    public Optional<Post> findById(Long postSeq, Long postWriterSeq, Long userSeq) {
        checkNotNull(postSeq, "postId must be provided.");
        checkNotNull(userSeq, "userId must be provided.");
        checkNotNull(postWriterSeq, "writerId must be provided.");

        return postRepository.findById(postSeq, userSeq, postWriterSeq);
    }

    @Transactional(readOnly = true)
    public List<Post> findAll(Long userSeq, Long postWriterSeq, Pageable pageable) {
        checkNotNull(userSeq, "userId must be provided.");
        checkNotNull(postWriterSeq, "userId must be provided.");

        userRepository.findBySeq(postWriterSeq)
                .orElseThrow(() -> new NotFoundException(User.class, postWriterSeq));
        // TODO likesOfMe를 효율적으로 구하기 위해 변경 필요
        return postRepository.findAll(userSeq, postWriterSeq, pageable);
    }


}
