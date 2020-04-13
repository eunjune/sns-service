package com.github.prgrms.social.api.model.post;

import com.github.prgrms.social.api.model.BaseTimeEntity;
import com.github.prgrms.social.api.model.user.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@NoArgsConstructor(force = true)
@Getter
@Setter
@ToString(exclude = {"post","user"})
@EqualsAndHashCode(of = "id",callSuper = false)
@Entity
public class Comment extends BaseTimeEntity {

    @ApiModelProperty(value = "PK", required = true)
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;

    @ApiModelProperty(value = "내용", required = true)
    @Column(nullable = false)
    private String content;

    @ManyToOne
    private Post post;

    @ManyToOne
    private User user;

    @Builder
    private Comment(Long id, String content) {
        checkArgument(isNotEmpty(content), "contents must be provided.");
        checkArgument(
                content.length() >= 4 && content.length() <= 500,
                "comment contents length must be between 4 and 500 characters."
        );

        this.id = id;
        this.content = content;
    }
}
