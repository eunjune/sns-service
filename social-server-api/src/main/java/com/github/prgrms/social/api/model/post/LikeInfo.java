package com.github.prgrms.social.api.model.post;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.github.prgrms.social.api.model.user.User;
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
public class LikeInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private final LocalDateTime createAt;

    @ManyToOne
    @Setter
    @JsonManagedReference
    private User user;

    @ManyToOne
    @Setter
    @JsonBackReference
    private Post post;

    public LikeInfo(Long id, LocalDateTime createAt) {
        this.id = id;
        this.createAt = defaultIfNull(createAt, now());
    }
}
