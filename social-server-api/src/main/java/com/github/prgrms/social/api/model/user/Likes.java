package com.github.prgrms.social.api.model.user;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.github.prgrms.social.api.model.post.Post;
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
@ToString(exclude = {"user","post"})
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long seq;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private final LocalDateTime createAt;

    @ManyToOne
    @Setter
    @JsonManagedReference
    private User user;

    @ManyToOne
    @Setter
    @JsonManagedReference
    private Post post;

    public Likes(Long seq, LocalDateTime createAt) {
        this.seq = seq;
        this.createAt = defaultIfNull(createAt, now());
    }
}
