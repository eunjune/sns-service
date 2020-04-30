package com.github.prgrms.social.api.repository.user;

import com.github.prgrms.social.api.model.user.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface NotificationRepository extends JpaRepository<Notification,Long> {

    Notification save(Notification notification);
}
