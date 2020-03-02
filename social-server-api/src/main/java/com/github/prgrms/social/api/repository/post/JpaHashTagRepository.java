package com.github.prgrms.social.api.repository.post;

import com.github.prgrms.social.api.model.post.HashTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaHashTagRepository extends JpaRepository<HashTag, Long> {

    HashTag save(HashTag hashTag);


}
