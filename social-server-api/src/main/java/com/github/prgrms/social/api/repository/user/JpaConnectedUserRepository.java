package com.github.prgrms.social.api.repository.user;

import com.github.prgrms.social.api.model.user.ConnectedUser;
import com.github.prgrms.social.api.repository.user.projection.ConnectedId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface JpaConnectedUserRepository extends JpaRepository<ConnectedUser, Long> {

    ConnectedUser save(ConnectedUser connectedUser);

    @Transactional(readOnly = true)
    List<ConnectedUser> findByUser_IdAndCreateAtIsNotNullOrderByIdDesc(Long id);

    @Transactional(readOnly = true)
    List<ConnectedId> findByUser_IdAndCreateAtIsNotNullOrderByTargetUser_Id(Long id);

    void deleteByUserIdAndTargetUserId(Long userId,Long targetUserId);
}
