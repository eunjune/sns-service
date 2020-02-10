package com.github.prgrms.social.api.model.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

@Entity
@NoArgsConstructor(force = true)
@Getter
@ToString(exclude = {"user","targetUser"})
public class ConnectedUser {

    @ApiModelProperty(value = "PK", required = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long seq;

    @ApiModelProperty(value = "승락일시", required = true)
    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private final LocalDateTime grantedAt;

    @ManyToOne
    @Setter
    private User user;

    @OneToOne
    @Setter
    private User targetUser;

    public ConnectedUser(Long seq, LocalDateTime grantedAt) {
        this.seq = seq;
        this.grantedAt = defaultIfNull(grantedAt, now());
    }
}
