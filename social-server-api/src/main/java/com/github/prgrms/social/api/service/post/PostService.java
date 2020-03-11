package com.github.prgrms.social.api.service.post;

import com.github.prgrms.social.api.error.NotFoundException;
import com.github.prgrms.social.api.model.commons.AttachedFile;
import com.github.prgrms.social.api.model.post.*;
import com.github.prgrms.social.api.model.user.User;
import com.github.prgrms.social.api.repository.post.*;
import com.github.prgrms.social.api.repository.user.JpaUserRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
public class PostService {

    private final JpaUserRepository userRepository;

    private final JpaPostRepository postRepository;

    private final JpaPostLikeRepository postLikeRepository;

    private final JpaHashTagRepository hashTagRepository;

    private final JpaImageRepository imageRepository;

    private final JpaRetweetRepository retweetRepository;

    public PostService(JpaUserRepository userRepository, JpaPostRepository postRepository, JpaPostLikeRepository postLikeRepository
            , JpaHashTagRepository hashTagRepository, JpaImageRepository imageRepository, JpaRetweetRepository retweetRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.postLikeRepository = postLikeRepository;
        this.hashTagRepository = hashTagRepository;
        this.imageRepository = imageRepository;
        this.retweetRepository = retweetRepository;
    }

    @Transactional
    public Post write(Post post, Long userId, List<String> imagePaths) {
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

                    for(String imagePath : imagePaths) {
                        Image image = Image.builder().path(imagePath).build();
                        image = imageRepository.save(image);
                        post.addImage(image);
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

                    Likes like = new Likes(null,null);
                    post.getUser().addLike(like);
                    post.incrementAndGetLikes(like);
                    postLikeRepository.save(like);
                }
            return post;
        });
    }

    @Transactional
    public Optional<Post> unlike(Long postId, Long userId, Long postWriterId) {
        checkNotNull(postId, "postId must be provided.");
        checkNotNull(userId, "userId must be provided.");
        checkNotNull(postWriterId, "writerId must be provided.");

        return postRepository.findByIdCustom(postId, userId, postWriterId)
                .map(post -> {
                    if (post.isLikesOfMe()) {
                        List<Likes> likes = post.getLikes();
                        post.setLikes(new ArrayList<>());

                        Likes deleteLike = null;
                        for(Likes like : likes) {
                            if(like.getUser().getId().equals(userId)) {
                                deleteLike = like;
                                continue;
                            }
                            post.getUser().addLike(like);
                            post.incrementAndGetLikes(like);
                        }

                        postLikeRepository.deleteById(deleteLike.getId());
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
    public List<Post> findAllById(Long userId, Long postWriterId, Long lastId, Pageable pageable) {
        checkNotNull(userId, "userId must be provided.");
        checkNotNull(postWriterId, "postWriterId must be provided.");
        checkNotNull(lastId, "lastId must be provided.");

        if(postWriterId == 0L) {
            return postRepository.findAllById(userId, userId, lastId , pageable);
        }

        userRepository.findById(postWriterId)
                .orElseThrow(() -> new NotFoundException(User.class, postWriterId));
        return postRepository.findAllById(userId, postWriterId, lastId, pageable);
    }

    @Transactional(readOnly = true)
    public List<Post> findAll(Long lastId, Pageable pageable) {

        if(lastId == 0L) {
            return postRepository.findAllByOrderByCreateAtDesc(pageable);
        }

        return postRepository.findAllByIdLessThanOrderByCreateAtDesc(lastId, pageable);
    }


    @Transactional(readOnly = true)
    public List<Post> findByHashTag(String tag, Long lastId, Pageable pageable) {
        checkNotNull(tag, "tag must be provided.");
        checkNotNull(lastId, "lastId must be provided.");

        if(lastId == 0L) {
            return hashTagRepository.findByName(tag)
                    .map(hashTag -> {
                        List<Post> posts = hashTag.getPosts();
                        posts.sort((o1, o2) -> o2.getId().compareTo(o1.getId()));
                        return posts.subList(0, Math.min(pageable.getPageSize(), posts.size()));
                    })
                    .orElseThrow(() -> new NotFoundException(HashTag.class, tag));
        }

        return hashTagRepository.findByName(tag)
                .map(hashTag -> {
                    List<Post> posts = hashTag.getPosts();
                    posts.sort((o1, o2) -> o2.getId().compareTo(o1.getId()));

                    int startIndex = 0;
                    for(int i=0; i<posts.size(); ++i) {
                        if(posts.get(i).getId().equals(lastId)) {
                            startIndex = i + 1;
                            break;
                        }
                    }
                    return posts.subList(startIndex, Math.min(pageable.getPageSize(), posts.size() - startIndex));
                })
                .orElseThrow(() -> new NotFoundException(HashTag.class, tag));
    }

    @Transactional
    public List<String> uploadImage(MultipartFile[] files, String realPath) throws IOException {
        // 20 * 1024 * 1024
        checkNotNull(files, "files must be provided.");

        realPath = realPath.substring(0,34) + "uploads";

        List<String> result = new ArrayList<>();
        for(MultipartFile file : files) {
            AttachedFile attachedFile = AttachedFile.toAttachedFile(file);
            assert attachedFile != null;
            String extension = attachedFile.extension("png");
            String randomName = attachedFile.randomName(realPath,extension);
            file.transferTo(new File(randomName));
            result.add(randomName.substring(realPath.length()+1));
        }

        return result;
    }

    @Transactional
    public Post retweet(Long postId, Long userId) {
        checkNotNull(postId, "postId must be provided.");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(User.class, userId));

        return postRepository.findById(postId)
                .map(post -> {
                    if(post.getUser().getId().equals(userId)) {
                        throw new IllegalArgumentException("자신의 글은 리트윗 할 수 없습니다.");
                    }

                    if(findRetweetByPostId(postId).isPresent()) {
                        throw new IllegalArgumentException("이미 리트윗 했습니다.");
                    }

                    Post retweetPost = Post.builder().content("retweet").build();
                    Retweet retweet = Retweet.builder().build();

                    retweetPost.addRetweet(retweet,post);

                    user.addPost(retweetPost);

                    return postRepository.save(retweetPost);
                })
                .orElseThrow(() -> new NotFoundException(Post.class, postId));
    }

    @Transactional(readOnly = true)
    public Optional<Retweet> findRetweetByPostId(Long postId) {
        checkNotNull(postId, "postId must be provided.");

        return retweetRepository.findByTargetPostId(postId);

    }

    public Long removePost(Long userId, Long postId) {
        checkNotNull(userId, "userId must be provided.");
        checkNotNull(postId, "postId must be provided.");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(User.class, userId));

        return postRepository.findById(postId)
                .map(post -> {
                    /*List<Post> posts = user.getPosts();
                    List<Post> newPosts = new ArrayList<>();
                    for(Post postItem : posts) {
                        if(postItem.getId().equals(postId)) {
                            continue;
                        }
                        newPosts.add(postItem);
                    }
                    Post newPosts*/

                    postRepository.deleteById(postId);

                    return post.getId();
                })
                .orElseThrow(() -> new NotFoundException(Post.class, postId));
    }
}
