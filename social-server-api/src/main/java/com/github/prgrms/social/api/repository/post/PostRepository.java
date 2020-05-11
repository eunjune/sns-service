package com.github.prgrms.social.api.repository.post;

import com.github.prgrms.social.api.model.post.Post;
import com.github.prgrms.social.api.repository.projection.PostProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Long> {

    Post save(Post post);

    @Transactional(readOnly = true)
    @EntityGraph(attributePaths = {"images","comments","likeInfos"})
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
    List<Post> findWithLikeAndImageWithCommentByUser_IdAndIdLessThan(Long postWriterId, Long lastId, Pageable pageable);


    @Transactional(readOnly = true)
    @EntityGraph(attributePaths = {"likeInfos","images","comments"})
    List<Post> findWithLikeAndImageWithCommentByIdLessThanAndUser_IsPrivateFalse(Long lastId, Pageable pageable);

    @Transactional(readOnly = true)
    PostProjection findUserById(Long id);


    @Transactional(readOnly = true)
    @Query(value = "SELECT p.*,r.target_post_id from hash_tag h " +
            "LEFT OUTER JOIN post_hashtag ph ON h.id=ph.hashtag_id " +
            "LEFT OUTER JOIN post p ON ph.post_id=p.id " +
            "LEFT OUTER JOIN retweet r ON p.id=r.post_id " +
            "LEFT OUTER JOIN users u ON p.user_id=u.id " +
            "WHERE h.name = :name AND p.id < :lastId AND u.is_private = false ORDER BY p.id DESC" , nativeQuery = true)
    List<Post> findWithHashtagByName(@Param("name") String name, @Param("lastId") Long lastId, Pageable pageable);

    @Transactional(readOnly = true)
    Optional<PostProjection> findFirstByOrderByIdDesc();

    @Transactional(readOnly = true)
    @EntityGraph(attributePaths = {"likeInfos","images","comments"})
    List<Post> findSearchByContentContainingAndIdLessThanAndUser_IsPrivateFalse(String keyword, Long lastId, Pageable pageable);
}
