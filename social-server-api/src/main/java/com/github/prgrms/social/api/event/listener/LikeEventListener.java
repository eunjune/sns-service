package com.github.prgrms.social.api.event.listener;

import com.github.prgrms.social.api.event.JoinEvent;
import com.github.prgrms.social.api.event.LikeEvent;
import com.github.prgrms.social.api.model.user.Notification;
import com.github.prgrms.social.api.model.user.NotificationType;
import com.github.prgrms.social.api.repository.user.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Column;

@Slf4j
@Async
@Component
@RequiredArgsConstructor
@Transactional
public class LikeEventListener {

    private final NotificationRepository notificationRepository;

    @EventListener
    public void handleJoinEvent(LikeEvent likeEvent) {
        log.info("user {} like post {}", likeEvent.getMe().getId(), likeEvent.getTargetPost().getId());

        Notification notification = Notification.builder()
                .message(likeEvent.getMe().getName() + "님이 게시글(" + likeEvent.getTargetPost().getId() + ")을 좋아합니다.")
                .subMessage(likeEvent.getTargetPost().getId() + "")
                .sender(likeEvent.getMe().getId())
                .senderProfileImage(likeEvent.getMe().getProfileImageUrl())
                .notificationType(NotificationType.LIKE)
                .build();

        notification.setUser(likeEvent.getTargetPost().getUser());

        notificationRepository.save(notification);
    }
}
