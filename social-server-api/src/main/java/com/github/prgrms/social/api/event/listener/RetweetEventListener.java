package com.github.prgrms.social.api.event.listener;

import com.github.prgrms.social.api.event.CommentEvent;
import com.github.prgrms.social.api.event.RetweetEvent;
import com.github.prgrms.social.api.model.user.Notification;
import com.github.prgrms.social.api.model.user.NotificationType;
import com.github.prgrms.social.api.repository.user.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Async
@Component
@RequiredArgsConstructor
@Transactional
public class RetweetEventListener {

    private final NotificationRepository notificationRepository;

    @EventListener
    public void handleJoinEvent(RetweetEvent retweetEvent) {

        log.info("user{} retweet post{}", retweetEvent.getMe().getId(), retweetEvent.getTargetPost().getId());

        Notification notification = Notification.builder()
                .message(retweetEvent.getMe().getName() + "님이 포스트(" + retweetEvent.getTargetPost().getId() + ")을 리트윗 하셨습니다.")
                .subMessage(retweetEvent.getTargetPost().getId() + "")
                .sender(retweetEvent.getMe().getId())
                .senderProfileImage(retweetEvent.getMe().getProfileImageUrl())
                .notificationType(NotificationType.RETWEET)
                .build();

        notification.setUser(retweetEvent.getTargetPost().getUser());
        notificationRepository.save(notification);
    }
}
