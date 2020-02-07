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
@EqualsAndHashCode(of = "seq")
@ToString
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long seq;

    @Column(nullable = false, unique = true)
    private final Long userSeq;

    @Column(nullable = false, unique = true)
    private final String endpoint;

    @Column(nullable = false)
    private final String publicKey;

    @Column(nullable = false)
    private final String auth;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime createAt;

    @Builder
    private Subscription(Long seq, Long userSeq, String endpoint, String publicKey, String auth, LocalDateTime createAt) {
        checkNotNull(userSeq, "userId must be provided.");
        checkArgument(isNotEmpty(endpoint), "endpoint must be provided.");
        checkArgument(isNotEmpty(publicKey), "publicKey must be provided.");
        checkArgument(isNotEmpty(auth), "auth must be provided.");

        this.seq = seq;
        this.userSeq = userSeq;
        this.endpoint = endpoint;
        this.publicKey = publicKey;
        this.auth = auth;
        this.createAt = defaultIfNull(createAt, now());
    }

/*
    public Subscription() {
        seq = null;
        userSeq = null;
        endpoint = null;
        publicKey = null;
        auth = null;
    }

    public Subscription(Long userSeq, String endpoint, String publicKey, String auth) {
        this(null, userSeq, endpoint, publicKey, auth, null);
    }



    public Long getSeq() {
        return seq;
    }

    public Long getUserSeq() {
        return userSeq;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getAuth() {
        return auth;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subscription subscription = (Subscription) o;
        return Objects.equals(seq, subscription.seq);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("seq", seq)
                .append("userSeq", userSeq)
                .append("endpoint", endpoint)
                .append("password", "[PROTECTED]")
                .append("publicKey", publicKey)
                .append("auth", auth)
                .append("createAt", createAt)
                .toString();
    }


    public static final class Builder {
        private Long seq;
        private Long userSeq;
        private String endpoint;
        private String publicKey;
        private String auth;
        private LocalDateTime createAt;

        public Builder() {
        }

        public Builder(Subscription subscription) {
            this.seq = subscription.seq;
            this.userSeq = subscription.userSeq;
            this.endpoint = subscription.endpoint;
            this.publicKey = subscription.publicKey;
            this.auth = subscription.auth;
            this.createAt = subscription.createAt;
        }

        public Builder seq(Long seq) {
            this.seq = seq;
            return this;
        }

        public Builder userSeq(Long userSeq) {
            this.userSeq = userSeq;
            return this;
        }

        public Builder endpoint(String endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        public Builder publicKey(String publicKey) {
            this.publicKey = publicKey;
            return this;
        }

        public Builder auth(String auth) {
            this.auth = auth;
            return this;
        }

        public Builder createAt(LocalDateTime createAt) {
            this.createAt = createAt;
            return this;
        }

        public Subscription build() {
            return new Subscription(seq,userSeq,endpoint,publicKey,auth,createAt);
        }
    }*/
}
