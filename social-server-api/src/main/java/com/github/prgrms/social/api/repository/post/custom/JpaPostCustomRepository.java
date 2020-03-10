package com.github.prgrms.social.api.repository.post.custom;

import com.github.prgrms.social.api.model.post.Post;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface JpaPostCustomRepository {

    Optional<Post> findByIdCustom(Long id, Long userId, Long postWriterId);

    List<Post> findAllById(Long userId, Long postWriterId, Pageable pageable);
}
