package com.github.prgrms.social.api.repository.post;

import com.github.prgrms.social.api.model.post.Post;
import com.github.prgrms.social.api.repository.post.custom.JpaPostCustomRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface JpaPostRepository extends JpaRepository<Post,Long>, JpaPostCustomRepository {

    Post save(Post post);

    @Transactional(readOnly = true)
    Optional<Post> findById(Long id);

    @Transactional(readOnly = true)
    @Override
    Optional<Post> findByIdCustom(Long id, Long userId, Long postWriterId);

    @Transactional(readOnly = true)
    @Override
    List<Post> findAllById(Long userId, Long postWriterId, Long lastId, Pageable pageable);

    @Transactional(readOnly = true)
    List<Post> findAllByIdLessThanOrderByCreateAtDesc(Long lastId, Pageable pageable);

    @Transactional(readOnly = true)
    List<Post> findAllByOrderByCreateAtDesc(Pageable pageable);
}
