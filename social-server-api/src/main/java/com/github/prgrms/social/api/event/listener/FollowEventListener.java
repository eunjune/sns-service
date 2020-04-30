package com.github.prgrms.social.api.event.listener;


import com.github.prgrms.social.api.event.FollowEvent;
import com.github.prgrms.social.api.event.JoinEvent;
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

@Slf4j
@Async
@Component
@RequiredArgsConstructor
@Transactional
public class FollowEventListener {

    private final NotificationRepository notificationRepository;

    @EventListener
    public void handleJoinEvent(FollowEvent followEvent) {

        log.info("user{} follow user{}", followEvent.getMe().getId(), followEvent.getTargetUser().getId());

        Notification notification = Notification.builder()
                .message(followEvent.getMe().getName() + "님이 팔로우 하셨습니다.")
                .sender(followEvent.getMe().getId())
                .senderProfileImage(followEvent.getMe().getProfileImageUrl())
                .notificationType(NotificationType.FOLLOW)
                .build();

        notification.setUser(followEvent.getTargetUser());
        notificationRepository.save(notification);
    }
}
