package com.github.prgrms.social.api.model.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

import static com.google.common.base.Preconditions.checkNotNull;
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

    /*public ConnectedUser(Long seq, String name, Email email, String profileImageUrl, LocalDateTime grantedAt) {
        checkNotNull(seq, "seq must be provided.");
        checkNotNull(name, "name must be provided.");
        checkNotNull(email, "email must be provided.");
        checkNotNull(grantedAt, "grantedAt must be provided.");

        this.seq = seq;
        this.name = name;
        this.email = email;
        this.grantedAt = grantedAt;
        this.profileImageUrl = profileImageUrl;
    }

    public Long getSeq() {
        return seq;
    }

    public String getName() {
        return name;
    }

    public Email getEmail() {
        return email;
    }

    public LocalDateTime getGrantedAt() {
        return grantedAt;
    }

    public Optional<String> getProfileImageUrl() {
        return ofNullable(profileImageUrl);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("seq", seq)
                .append("name", name)
                .append("email", email)
                .append("grantedAt", grantedAt)
                .append("profileImageUrl", profileImageUrl)
                .toString();
    }*/

}
