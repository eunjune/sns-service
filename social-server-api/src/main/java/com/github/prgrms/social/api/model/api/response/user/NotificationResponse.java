package com.github.prgrms.social.api.model.api.response.user;

import com.github.prgrms.social.api.model.user.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {

    private Long id;

    private String message;

    private String subMessage;

    private boolean readMark;

    private Long sender;

    private String senderProfileImage;

    private NotificationType notificationType;

    private LocalDateTime createdAt;
}
