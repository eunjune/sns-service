package com.github.prgrms.social.api.repository.post;

import com.github.prgrms.social.api.model.post.HashTag;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface HashTagRepository extends JpaRepository<HashTag, Long> {

    HashTag save(HashTag hashTag);

    @Transactional(readOnly = true)
    List<HashTag> findAll();

    @Transactional(readOnly = true)
    @EntityGraph(attributePaths = "posts")
    Optional<HashTag> findByName(String name);

}
