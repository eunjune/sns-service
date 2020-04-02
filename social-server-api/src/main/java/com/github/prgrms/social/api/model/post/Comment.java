package com.github.prgrms.social.api.model.post;

import com.github.prgrms.social.api.model.user.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

import static com.google.common.base.Preconditions.checkArgument;
import static java.time.LocalDateTime.now;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Entity
@Getter
@NoArgsConstructor(force = true)
@ToString(exclude = {"post","user"})
@EqualsAndHashCode(of = "id")
public class Comment {

    @ApiModelProperty(value = "PK", required = true)
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;

    @ApiModelProperty(value = "내용", required = true)
    @Column(nullable = false)
    private String content;

    @ApiModelProperty(value = "작성일시", required = true)
    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private final LocalDateTime createAt;

    @ApiModelProperty(value = "댓글의 포스트")
    @ManyToOne
    @Setter
    private Post post;

    @ApiModelProperty(value = "댓글 작성자")
    @ManyToOne
    @Setter
    private User user;

    @Builder
    private Comment(Long id, String content, LocalDateTime createAt) {
        checkArgument(isNotEmpty(content), "contents must be provided.");
        checkArgument(
                content.length() >= 4 && content.length() <= 500,
                "comment contents length must be between 4 and 500 characters."
        );

        this.id = id;
        this.content = content;
        this.createAt = defaultIfNull(createAt, now());
    }

    public void modify(String contents) {
        checkArgument(isNotEmpty(contents), "contents must be provided.");
        checkArgument(
                contents.length() >= 4 && contents.length() <= 500,
                "post contents length must be between 4 and 500 characters."
        );

        this.content = contents;
    }
}
