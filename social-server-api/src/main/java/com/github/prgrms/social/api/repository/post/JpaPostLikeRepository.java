package com.github.prgrms.social.api.repository.post;

import com.github.prgrms.social.api.model.user.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPostLikeRepository extends JpaRepository<Likes, Long> {

    Likes save(Likes likes);

}
