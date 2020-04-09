package com.github.prgrms.social.api.service.post;

import com.github.prgrms.social.api.error.NotFoundException;
import com.github.prgrms.social.api.error.UnauthorizedException;
import com.github.prgrms.social.api.model.api.request.post.PostingRequest;
import com.github.prgrms.social.api.model.commons.AttachedFile;
import com.github.prgrms.social.api.model.post.HashTag;
import com.github.prgrms.social.api.model.post.Image;
import com.github.prgrms.social.api.model.post.LikeInfo;
import com.github.prgrms.social.api.model.post.Post;
import com.github.prgrms.social.api.model.user.User;
import com.github.prgrms.social.api.repository.post.HashTagRepository;
import com.github.prgrms.social.api.repository.post.ImageRepository;
import com.github.prgrms.social.api.repository.post.PostLikeRepository;
import com.github.prgrms.social.api.repository.post.PostRepository;
import com.github.prgrms.social.api.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
@RequiredArgsConstructor
public class PostService {

    private final UserRepository userRepository;

    private final PostRepository postRepository;

    private final PostLikeRepository postLikeRepository;

    private final HashTagRepository hashTagRepository;

    private final ImageRepository imageRepository;


    @Transactional(readOnly = true)
    public Optional<Post> getPost(Long postId) {
        checkNotNull(postId, "postId must be provided.");

        return postRepository.findById(postId);
    }

    @Transactional(readOnly = true)
    public Optional<Post> getPost(Long postId, Long postWriterId, Long userId) {
        checkNotNull(postId, "postId must be provided.");
        checkNotNull(userId, "userId must be provided.");
        checkNotNull(postWriterId, "writerId must be provided.");

        return postRepository.findByIdAndUser_Id(postId, postWriterId);
    }

    @Transactional(readOnly = true)
    public Optional<Post> getPostWithLike(Long postId, Long postWriterId, Long userId) {
        checkNotNull(postId, "postId must be provided.");
        checkNotNull(userId, "userId must be provided.");
        checkNotNull(postWriterId, "writerId must be provided.");

        return postRepository.findWithLikeByIdAndUser_Id(postId, postWriterId)
                .map(post -> {
                    post.setLikesOfMe(userId);
                    return post;
                });
    }

    @Transactional(readOnly = true)
    public List<Post> getPostsWithImageAndLikeWithComment(Long userId, Long postWriterId, Long lastId, Pageable pageable) {
        checkNotNull(userId, "userId must be provided.");
        checkNotNull(lastId, "lastId must be provided.");

        userRepository.findById(postWriterId)
                .orElseThrow(() -> new NotFoundException(User.class, postWriterId));

        if(lastId == 0L) {
            return postRepository.findWithLikeAndImageWithCommentByUser_IdOrderByIdDesc(postWriterId, pageable)
                    .stream()
                    .peek(post -> post.setLikesOfMe(userId))
                    .collect(Collectors.toList());
        }

        return postRepository.findWithLikeAndImageWithCommentByUser_IdAndIdLessThanOrderByIdDesc(postWriterId, lastId, pageable)
                .stream()
                .peek(post -> post.setLikesOfMe(userId))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Post> getPostsWithImageAndLikeWithComment(Long lastId, Pageable pageable) {
        checkNotNull(lastId, "lastId must be provided.");

        if(lastId == 0L) {
            return postRepository.findWithLikeAndImageWithCommentByUser_IsPrivateFalseOrderByIdDesc(pageable);
        }

        return postRepository.findWithLikeAndImageWithCommentByIdLessThanAndUser_IsPrivateFalseOrderByIdDesc(lastId, pageable);
    }

    // !follwings && isPrivate
    @Transactional(readOnly = true)
    public List<Post> getPostsWithImageAndLikeWithComment(Long userId, Long lastId, Pageable pageable) {
        checkNotNull(userId, "userId must be provided.");
        checkNotNull(lastId, "lastId must be provided.");

        /*if(lastId == 0L) {

        }


        return userRepository.findById(userId)
                .map(user -> {
                    List<Post> list = lastId == 0L ? postRepository.findAllByOrderByIdDesc(pageable) :
                            postRepository.findAllByIdLessThanOrderByIdDesc(lastId, pageable);
                    return list.stream()
                            .filter(post -> user.getFollowings().contains(post.getUser()) || !post.getUser().isPrivate())
                            .collect(Collectors.toList());

                })
                .orElseThrow(() -> new NotFoundException(User.class, userId));*/
        return null;

    }

    @Transactional
    public Post write(Post post, Long userId, Set<String> imagePaths) {
        checkNotNull(post, "post must be provided.");
        checkNotNull(userId, "userId must be provided.");

        return userRepository.findById(userId)
                .map(user -> {

                    if(!user.isEmailCertification()) {
                        throw new UnauthorizedException("인증된 사용자만 사용할 수 있습니다.");
                    }

                    user.addPost(post);
                    return postRepository.save(post);
                })
                .map(savedPost -> {

                    List<HashTag> hashTagList = savedPost.findHashTag();
                    for(HashTag item : hashTagList) {
                        HashTag hashTag = hashTagRepository.findByName(item.getName()).orElse(null);
                        if(hashTag == null) {
                            hashTag = hashTagRepository.save(item);
                        }
                        hashTag.getPosts().add(savedPost);
                    }

                    for(String imagePath : imagePaths) {
                        Image image = Image.builder().path(imagePath).build();
                        savedPost.addImage(image);
                        imageRepository.save(image);
                    }

                    return savedPost;
                })
                .orElseThrow(() -> new NotFoundException(User.class, userId));
    }



    // 좋아요 기능 처리
    @Transactional
    public Post like(Long postId, Long userId, Long postWriterId) {
        checkNotNull(postId, "postId must be provided.");
        checkNotNull(userId, "userId must be provided.");
        checkNotNull(postWriterId, "postWriterId must be provided.");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Long.class,userId));

        return postRepository.findWithLikeByIdAndUser_Id(postId, postWriterId)
            .map(post -> {
                if (!post.isLikesOfMe()) {

                    LikeInfo likeInfo = LikeInfo.builder().build();
                    post.incrementAndGetLikes(likeInfo);
                    likeInfo.setUser(user);
                }
                return post;
            })
            .orElseThrow(() -> new NotFoundException(Post.class, postId));
    }

    // TODO : postWriterId 삭제
    @Transactional
    public Post unlike(Long postId, Long userId, Long postWriterId) {
        checkNotNull(postId, "postId must be provided.");
        checkNotNull(userId, "userId must be provided.");
        checkNotNull(postWriterId, "postWriterId must be provided.");

        LikeInfo likeInfo = postLikeRepository.findByUser_IdAndPost_Id(userId,postId).orElseThrow(() -> new NotFoundException(LikeInfo.class,userId,postId));

        return getPostWithLike(postId, postWriterId,userId)
                .map(post -> {
                    if (post.isLikesOfMe()) {
                        likeInfo.setUser(null);
                        post.removeLikes(likeInfo);
                    }

                    return post;
                })
                .orElseThrow(() -> new NotFoundException(Post.class, postId));
    }




    @Transactional
    public Post retweet(Long postId, Long userId) {
        checkNotNull(postId, "postId must be provided.");
        checkNotNull(userId, "userId must be provided.");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(User.class, userId));

        return postRepository.findById(postId)
                .map(post -> {

                    if(post.getRetweetPost() != null) {
                        throw new IllegalArgumentException("이미 리트윗 했습니다.");
                    }

                    if(post.getUser().getId().equals(userId)) {
                        throw new IllegalArgumentException("자신의 글은 리트윗 할 수 없습니다.");
                    }

                    Post retweetPost = postRepository.save(Post.builder().content("retweet").build());

                    retweetPost.setUser(user);
                    retweetPost.addRetweet(post);

                    return retweetPost;
                })
                .orElseThrow(() -> new NotFoundException(Post.class, postId));
    }

    @Transactional
    public Post updatePost(Long postId, PostingRequest postingRequest) {
        checkNotNull(postId, "postId must be provided.");
        checkNotNull(postingRequest, "postingRequest must be provided.");

        return postRepository.findWithImageById(postId)
                .map(post -> {
                    post.setContent(postingRequest.getContent());

                    Map<String,Image> removeImageMap = new HashMap();
                    for(Image image : post.getImages()){
                        removeImageMap.put(image.getPath(),image);
                    }

                    for(String key : removeImageMap.keySet()) {
                        if(!postingRequest.getImagePaths().contains(key)) {
                            post.removeImage(removeImageMap.get(key));
                            imageRepository.deleteById(removeImageMap.get(key).getId());
                        }
                    }

                    for(String path: postingRequest.getImagePaths()) {
                        if(imageRepository.existsByPath(path)) {
                            continue;
                        }

                        Image savedImage =  imageRepository.save(Image.builder().path(path).build());
                        post.addImage(savedImage);
                    }
                    return post;
                })
                .orElseThrow(() -> new NotFoundException(Post.class, postId));
    }

    @Transactional
    public Long removePost(Long userId, Long postId) {
        checkNotNull(userId, "userId must be provided.");
        checkNotNull(postId, "postId must be provided.");

        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(User.class, userId));

        return postRepository.findById(postId)
                .map(post -> {
                    postRepository.deleteById(postId);
                    return post.getId();
                })
                .orElseThrow(() -> new NotFoundException(Post.class, postId));
    }

    public List<String> uploadImage(MultipartFile[] files, String realPath) throws IOException {
        // 20 * 1024 * 1024
        checkNotNull(files, "files must be provided.");

        //Todo 메소드 분리
        //Todo 배포시 변경 필요
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
}
