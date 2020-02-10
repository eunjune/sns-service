package com.github.prgrms.social.api.repository.post.custom;

import com.github.prgrms.social.api.model.post.Post;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface JpaPostCustomRepository {

    Optional<Post> findById(Long seq, Long userSeq, Long postWriterSeq);

    List<Post> findAll(Long userSeq, Long postWriterSeq, Pageable pageable);
}
