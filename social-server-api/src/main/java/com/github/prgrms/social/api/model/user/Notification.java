package com.github.prgrms.social.api.model.user;

import com.github.prgrms.social.api.model.BaseTimeEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(force = true)
@Getter
@Setter
@EqualsAndHashCode(of="id", callSuper = false)
@ToString(exclude = "user")
@Entity
public class Notification extends BaseTimeEntity {

    @ApiModelProperty(value = "PK", required = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;

    @ApiModelProperty(value = "알림 메시지", required = true)
    private String message;

    @ApiModelProperty(value = "알림 메시지(추가)")
    private String subMessage;

    @ApiModelProperty(value = "읽음", required = true)
    private boolean readMark;

    @ApiModelProperty(value = "알림 송신자", required = true)
    private Long sender;

    @ApiModelProperty(value = "알림 송신자 프로필 이미지", required = true)
    private String senderProfileImage;

    @ApiModelProperty(value = "알림 유형", required = true)
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @ManyToOne
    private User user;

    @Builder
    private Notification(Long id, String message, String subMessage, Long sender,String senderProfileImage, NotificationType notificationType) {
        this.id = id;
        this.message = message;
        this.subMessage = subMessage;
        this.sender = sender;
        this.senderProfileImage = senderProfileImage;
        this.notificationType = notificationType;
    }
}
