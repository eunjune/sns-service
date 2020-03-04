package com.github.prgrms.social.api.model.user;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.time.LocalDateTime.now;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Entity
@NoArgsConstructor(force = true)
@Getter
@EqualsAndHashCode(of = "id")
@ToString
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;

    @Column(nullable = false, unique = true)
    private final Long userId;

    @Column(nullable = false, unique = true)
    private final String endpoint;

    @Column(nullable = false)
    private final String publicKey;

    @Column(nullable = false)
    private final String auth;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime createAt;

    @Builder
    private Subscription(Long id, Long userId, String endpoint, String publicKey, String auth, LocalDateTime createAt) {
        checkNotNull(userId, "userId must be provided.");
        checkArgument(isNotEmpty(endpoint), "endpoint must be provided.");
        checkArgument(isNotEmpty(publicKey), "publicKey must be provided.");
        checkArgument(isNotEmpty(auth), "auth must be provided.");

        this.id = id;
        this.userId = userId;
        this.endpoint = endpoint;
        this.publicKey = publicKey;
        this.auth = auth;
        this.createAt = defaultIfNull(createAt, now());
    }
}
