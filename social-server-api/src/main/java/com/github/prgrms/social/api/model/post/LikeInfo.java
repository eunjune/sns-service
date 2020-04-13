package com.github.prgrms.social.api.model.post;

import com.github.prgrms.social.api.model.BaseTimeEntity;
import com.github.prgrms.social.api.model.user.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(force = true)
@Getter
@Setter
@ToString(exclude = {"user","post"})
@Entity
public class LikeInfo extends BaseTimeEntity {

    @ApiModelProperty(value = "PK", required = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Post post;

    @Builder
    private LikeInfo(Long id) {
        this.id = id;
    }
}
