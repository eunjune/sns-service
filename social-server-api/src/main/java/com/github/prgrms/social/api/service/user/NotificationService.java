package com.github.prgrms.social.api.service.user;

import com.github.prgrms.social.api.error.NotFoundException;
import com.github.prgrms.social.api.model.user.Notification;
import com.github.prgrms.social.api.repository.user.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional(readOnly = true)
    public List<Notification> getNewNotification(Long userId) {
        checkNotNull(userId, "userId must be provided.");

        return notificationRepository.findByUser_IdAndReadMarkFalseOrderByIdDesc(userId);
    }

    @Transactional(readOnly = true)
    public List<Notification> getReadNotification(Long userId) {
        checkNotNull(userId, "userId must be provided.");

        return notificationRepository.findByUser_IdAndReadMarkTrueOrderByIdDesc(userId);
    }

    @Transactional
    public Long deleteNotification(Long notificationId) {
        checkNotNull(notificationId, "notificationId must be provided.");

        return notificationRepository.findById(notificationId)
                .map(notification -> {
                    notificationRepository.deleteById(notificationId);
                    return notificationId;
                })
                .orElseThrow(() -> new NotFoundException(Notification.class, notificationId));
    }

    @Transactional
    public Notification readCheck(Long notificationId) {
        checkNotNull(notificationId, "notificationId must be provided.");

        return notificationRepository.findById(notificationId)
                .map(notification -> {
                    notification.setReadMark(true);
                    return notification;
                })
                .orElseThrow(() -> new NotFoundException(Notification.class, notificationId));
    }



}
