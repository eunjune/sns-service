package com.github.prgrms.social.api.repository.user;

import com.github.prgrms.social.api.model.user.Notification;
import com.github.prgrms.social.api.repository.projection.NotificationProjection;
import com.github.prgrms.social.api.repository.projection.PostProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


public interface NotificationRepository extends JpaRepository<Notification,Long> {

    Notification save(Notification notification);

    List<Notification> findByUser_IdAndReadMarkFalseOrderByIdDesc(Long id);

    List<Notification> findByUser_IdAndReadMarkTrueOrderByIdDesc(Long id);

    Optional<Notification> findById(Long id);

    @Transactional(readOnly = true)
    NotificationProjection findUserById(Long id);
}
