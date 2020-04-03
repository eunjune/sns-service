package com.github.prgrms.social.api.repository.post;

import com.github.prgrms.social.api.model.post.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface JpaPostRepository extends JpaRepository<Post,Long> {

    Post save(Post post);

    @Transactional(readOnly = true)
    long countByUser_Id(Long id);

    @Transactional(readOnly = true)
    Optional<Post> findById(Long id);

    @Transactional(readOnly = true)
    @EntityGraph(attributePaths = {"likeInfos","images"})
    Optional<Post> findByIdAndUser_Id(Long id, Long userId);

    @Transactional(readOnly = true)
    @EntityGraph(attributePaths = {"likeInfos","images"})
    List<Post> findAllByUser_IdAndIdLessThanOrderByIdDesc(Long postWriterId, Long lastId, Pageable pageable);

    @Transactional(readOnly = true)
    @EntityGraph(attributePaths = {"likeInfos","images"})
    List<Post> findAllByUser_IdOrderByIdDesc(Long postWriterId, Pageable pageable);


    @Transactional(readOnly = true)
    @EntityGraph(attributePaths = {"likeInfos","images"})
    List<Post> findAllByIdLessThanAndUser_IsPrivateFalseOrderByIdDesc(Long lastId, Pageable pageable);

    @Transactional(readOnly = true)
    @EntityGraph(attributePaths = {"likeInfos","images"})
    List<Post> findAllByUser_IsPrivateFalseOrderByIdDesc(Pageable pageable);
}
