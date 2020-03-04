package com.github.prgrms.social.api.service.post;

import com.github.prgrms.social.api.error.NotFoundException;
import com.github.prgrms.social.api.model.post.HashTag;
import com.github.prgrms.social.api.model.post.Post;
import com.github.prgrms.social.api.model.user.Likes;
import com.github.prgrms.social.api.model.user.User;
import com.github.prgrms.social.api.repository.post.JpaHashTagRepository;
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

    private final JpaHashTagRepository hashTagRepository;

    public PostService(JpaUserRepository userRepository, JpaPostRepository postRepository, JpaPostLikeRepository postLikeRepository, JpaHashTagRepository hashTagRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.postLikeRepository = postLikeRepository;
        this.hashTagRepository = hashTagRepository;
    }

    @Transactional
    public Post write(Post post, Long userId) {
        return userRepository.findById(userId)
                .map(user -> {

                    List<HashTag> hashTagList = post.findHashTag();
                    for(HashTag item : hashTagList) {
                        HashTag hashTag = hashTagRepository.findByName(item.getName()).orElse(null);
                        if(hashTag == null) {
                            hashTag = hashTagRepository.save(item);
                        }
                        post.addHashTag(hashTag);
                    }
                    user.addPost(post);
                    return postRepository.save(post);
                })
                .orElseThrow(() -> new NotFoundException(User.class, userId));
    }

    // 좋아요 기능 처리
    @Transactional
    public Optional<Post> like(Long postId, Long userId, Long postWriterId) {
        // TODO PostLikeRepository를 구현하고, 포스트 좋아요 서비스를 구현하세요.
        checkNotNull(postId, "postId must be provided.");
        checkNotNull(userId, "userId must be provided.");
        checkNotNull(postWriterId, "writerId must be provided.");

        return postRepository.findByIdCustom(postId, userId, postWriterId)
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
    public Optional<Post> findById(Long postId, Long postWriterId, Long userId) {
        checkNotNull(postId, "postId must be provided.");
        checkNotNull(userId, "userId must be provided.");
        checkNotNull(postWriterId, "writerId must be provided.");

        return postRepository.findByIdCustom(postId, userId, postWriterId);
    }

    @Transactional(readOnly = true)
    public List<Post> findAll(Long userId, Long postWriterId, Pageable pageable) {
        checkNotNull(userId, "userId must be provided.");
        checkNotNull(postWriterId, "userId must be provided.");

        userRepository.findById(postWriterId)
                .orElseThrow(() -> new NotFoundException(User.class, postWriterId));
        // TODO likesOfMe를 효율적으로 구하기 위해 변경 필요
        return postRepository.findAll(userId, postWriterId, pageable);
    }

    @Transactional(readOnly = true)
    public List<Post> findByHashTag(Long userId, String tag, Pageable pageable) {
        checkNotNull(userId, "userId must be provided.");
        checkNotNull(tag, "tag must be provided.");

        return hashTagRepository.findByName(tag)
                .map(HashTag::getPosts)
                .orElseThrow(() -> new NotFoundException(HashTag.class, tag));
    }
}
