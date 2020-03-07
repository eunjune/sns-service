package com.github.prgrms.social.api.repository.post;

import com.github.prgrms.social.api.model.post.Retweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface JpaRetweetRepository extends JpaRepository<Retweet, Long> {

    Retweet save(Retweet retweet);

    @Transactional(readOnly = true)
    Optional<Retweet> findByTargetPostId(Long targetPostId);
}
