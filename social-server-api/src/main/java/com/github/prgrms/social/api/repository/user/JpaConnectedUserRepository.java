package com.github.prgrms.social.api.repository.user;

import com.github.prgrms.social.api.model.user.ConnectedUser;
import com.github.prgrms.social.api.repository.user.projection.ConnectedId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface JpaConnectedUserRepository extends JpaRepository<ConnectedUser, Long> {

    ConnectedUser save(ConnectedUser connectedUser);

    @Transactional(readOnly = true)
    List<ConnectedUser> findByUser_IdAndCreateAtIsNotNullOrderByIdDesc(Long id, Pageable pageable);

    @Transactional(readOnly = true)
    List<ConnectedUser> findByTargetUser_IdAndCreateAtIsNotNullOrderByIdDesc(Long id, Pageable pageable);

    @Transactional(readOnly = true)
    List<ConnectedId> findByUser_IdAndCreateAtIsNotNullOrderByTargetUser_Id(Long id);

    void deleteByUser_IdAndTargetUser_Id(Long userId, Long targetUserId);
}
