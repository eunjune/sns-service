package com.github.prgrms.social.api.model.post;

import com.github.prgrms.social.api.model.user.User;
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
@ToString(exclude = {"user","post"})
public class LikeInfo {

    @ApiModelProperty(value = "PK", required = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;

    @ApiModelProperty(value = "생성일시", required = true)
    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private final LocalDateTime createAt;

    @ManyToOne
    @Setter
    private User user;

    @ManyToOne
    @Setter
    private Post post;

    public LikeInfo(Long id, LocalDateTime createAt) {
        this.id = id;
        this.createAt = defaultIfNull(createAt, now());
    }
}
