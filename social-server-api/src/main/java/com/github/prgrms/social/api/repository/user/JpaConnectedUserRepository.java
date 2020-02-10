package com.github.prgrms.social.api.repository.user;

import com.github.prgrms.social.api.model.user.ConnectedUser;
import com.github.prgrms.social.api.repository.post.projection.ConnectedId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface JpaConnectedUserRepository extends JpaRepository<ConnectedUser, Long> {

    @Transactional(readOnly = true)
    List<ConnectedUser> findByUser_SeqAndGrantedAtIsNotNullOrderBySeqDesc(Long seq);

    @Transactional(readOnly = true)
    List<ConnectedId> findByUser_SeqAndGrantedAtIsNotNullOrderByTargetUser_Seq(Long seq);
}
