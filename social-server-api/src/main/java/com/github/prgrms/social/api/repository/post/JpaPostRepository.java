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
    Optional<Post> findBySeq(Long seq);

    @Transactional(readOnly = true)
    @Override
    Optional<Post> findById(Long aLong);

    @Transactional(readOnly = true)
    @Override
    List<Post> findAll(Long userSeq, Long postWriterSeq, Pageable pageable);
}
