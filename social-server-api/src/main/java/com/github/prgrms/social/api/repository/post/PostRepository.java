package com.github.prgrms.social.api.repository.post;

import com.github.prgrms.social.api.model.post.Post;
import com.github.prgrms.social.api.repository.projection.PostProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Long> {

    Post save(Post post);

    @Transactional(readOnly = true)
    Optional<Post> findById(Long id);

    @Transactional(readOnly = true)
    Optional<Post> findByIdAndUser_Id(Long id, Long userId);

    @Transactional(readOnly = true)
    @EntityGraph(attributePaths = {"images"})
    Optional<Post> findWithImageById(Long id);

    @Transactional(readOnly = true)
    @EntityGraph(attributePaths = {"likeInfos"})
    Optional<Post> findWithLikeByIdAndUser_Id(Long id, Long userId);

    @Transactional(readOnly = true)
    @EntityGraph(attributePaths = {"comments"})
    Optional<Post> findWithCommentByIdAndUser_Id(Long id, Long userId);


    @Transactional(readOnly = true)
    @EntityGraph(attributePaths = {"likeInfos","images","comments"})
    List<Post> findWithLikeAndImageWithCommentByUser_IdAndIdLessThanOrderByIdDesc(Long postWriterId, Long lastId, Pageable pageable);

    @Transactional(readOnly = true)
    @EntityGraph(attributePaths = {"likeInfos","images","comments"})
    List<Post> findWithLikeAndImageWithCommentByUser_IdOrderByIdDesc(Long postWriterId, Pageable pageable);

    @Transactional(readOnly = true)
    @EntityGraph(attributePaths = {"likeInfos","images"})
    List<Post> findAllByIdLessThanOrderByIdDesc(Long lastId, Pageable pageable);

    @Transactional(readOnly = true)
    @EntityGraph(attributePaths = {"likeInfos","images"})
    List<Post> findAllByOrderByIdDesc(Pageable pageable);


    @Transactional(readOnly = true)
    @EntityGraph(attributePaths = {"likeInfos","images","comments"})
    List<Post> findWithLikeAndImageWithCommentByIdLessThanAndUser_IsPrivateFalseOrderByIdDesc(Long lastId, Pageable pageable);

    @Transactional(readOnly = true)
    @EntityGraph(attributePaths = {"likeInfos","images","comments"})
    List<Post> findWithLikeAndImageWithCommentByUser_IsPrivateFalseOrderByIdDesc(Pageable pageable);

    @Transactional(readOnly = true)
    PostProjection findUserById(Long id);



}
