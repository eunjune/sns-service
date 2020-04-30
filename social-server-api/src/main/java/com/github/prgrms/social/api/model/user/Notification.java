package com.github.prgrms.social.api.model.user;

import com.github.prgrms.social.api.model.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(force = true)
@Getter
@Setter
@EqualsAndHashCode(of="id", callSuper = false)
@ToString(exclude = "user")
@Entity
public class Notification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    private String subMessage;

    private boolean readMark;

    private Long sender;

    private String senderProfileImage;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @ManyToOne
    private User user;

    @Builder
    private Notification(String message, String subMessage, Long sender,String senderProfileImage, NotificationType notificationType) {
        this.message = message;
        this.subMessage = subMessage;
        this.sender = sender;
        this.senderProfileImage = senderProfileImage;
        this.notificationType = notificationType;
    }
}
