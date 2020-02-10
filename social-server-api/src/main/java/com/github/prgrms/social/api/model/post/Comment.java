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
@EqualsAndHashCode(of = "seq")
public class Comment {

    @ApiModelProperty(value = "PK", required = true)
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long seq;

    @ApiModelProperty(value = "내용", required = true)
    @Column(nullable = false)
    private String contents;

    @ApiModelProperty(value = "작성일시", required = true)
    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private final LocalDateTime createAt;

    @ManyToOne
    @Setter
    private Post post;

    @ManyToOne
    @Setter
    private User user;

    @Builder
    private Comment(Long seq, String contents, LocalDateTime createAt) {
        checkArgument(isNotEmpty(contents), "contents must be provided.");
        checkArgument(
                contents.length() >= 4 && contents.length() <= 500,
                "comment contents length must be between 4 and 500 characters."
        );

        this.seq = seq;
        this.contents = contents;
        this.createAt = defaultIfNull(createAt, now());
    }

    public void modify(String contents) {
        checkArgument(isNotEmpty(contents), "contents must be provided.");
        checkArgument(
                contents.length() >= 4 && contents.length() <= 500,
                "post contents length must be between 4 and 500 characters."
        );

        this.contents = contents;
    }
}
