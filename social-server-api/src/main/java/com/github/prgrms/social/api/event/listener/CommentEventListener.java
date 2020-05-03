package com.github.prgrms.social.api.event.listener;

import com.github.prgrms.social.api.event.CommentEvent;
import com.github.prgrms.social.api.event.FollowEvent;
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
public class CommentEventListener{

    private final NotificationRepository notificationRepository;

    @EventListener
    public void handleJoinEvent(CommentEvent commentEvent) {

        log.info("user{} comment post{}", commentEvent.getMe().getId(), commentEvent.getTargetPost().getId());

        Notification notification = Notification.builder()
                .message(commentEvent.getMe().getName() + "님이 게시글(" + commentEvent.getTargetPost().getId() + ")에 대해 댓글을 작성했습니다.")
                .subMessage(commentEvent.getComment() + "," + commentEvent.getTargetPost().getId())
                .sender(commentEvent.getMe().getId())
                .senderProfileImage(commentEvent.getMe().getProfileImageUrl())
                .notificationType(NotificationType.COMMENT)
                .build();

        notification.setUser(commentEvent.getTargetPost().getUser());
        notificationRepository.save(notification);
    }
}
