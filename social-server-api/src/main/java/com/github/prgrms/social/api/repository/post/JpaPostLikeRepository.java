package com.github.prgrms.social.api.repository.post;

import com.github.prgrms.social.api.model.post.LikeInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface JpaPostLikeRepository extends JpaRepository<LikeInfo, Long> {

    LikeInfo save(LikeInfo likeInfo);

    @Transactional(readOnly = true)
    Optional<LikeInfo> findByUser_IdAndPost_Id(Long userId, Long postId);

    void deleteByUser_IdAndPost_Id(Long userId, Long postId);
}
