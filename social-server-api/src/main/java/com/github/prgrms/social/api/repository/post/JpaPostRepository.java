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
    Optional<Post> findById(Long id);

    @Transactional(readOnly = true)
    @EntityGraph(attributePaths = {"likeInfos","images", "comments"})
    Optional<Post> findByIdAndUser_Id(Long id, Long userId);

    @Transactional(readOnly = true)
    @EntityGraph(attributePaths = {"likeInfos","images", "comments"})
    List<Post> findAllByUser_IdAndIdLessThanOrderByIdDesc(Long postWriterId, Long lastId, Pageable pageable);

    @Transactional(readOnly = true)
    @EntityGraph(attributePaths = {"likeInfos","images", "comments"})
    List<Post> findAllByUser_IdOrderByIdDesc(Long postWriterId, Pageable pageable);


    @Transactional(readOnly = true)
    List<Post> findAllByIdLessThanOrderByIdDesc(Long lastId, Pageable pageable);

    @Transactional(readOnly = true)
    List<Post> findAllByOrderByIdDesc(Pageable pageable);
}
