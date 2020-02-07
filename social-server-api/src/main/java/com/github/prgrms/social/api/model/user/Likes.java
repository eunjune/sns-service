package com.github.prgrms.social.api.model.user;

import com.github.prgrms.social.api.model.post.Post;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.time.LocalDateTime.now;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

@Entity
@NoArgsConstructor(force = true)
@Getter
@ToString(exclude = {"user","post"})
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long seq;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private final LocalDateTime createAt;

    @ManyToOne
    @Setter
    private User user;

    @ManyToOne
    @Setter
    private Post post;

    public Likes(Long seq, LocalDateTime createAt) {
        this.seq = seq;
        this.createAt = defaultIfNull(createAt, now());
    }
}
