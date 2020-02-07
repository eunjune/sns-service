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

    /*




    public Long getSeq() {
        return seq;
    }

    public Id<User, Long> getUserId() {
        return userId;
    }

    public Id<Post, Long> getPostId() {
        return postId;
    }

    public String getContents() {
        return contents;
    }



    public LocalDateTime getCreateAt() {
        return createAt;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(seq, comment.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("seq", seq)
                .append("userId", userId)
                .append("postId", postId)
                .append("contents", contents)
                .append("writer", writer)
                .append("createAt", createAt)
                .toString();
    }


    static public class Builder {
        private Long seq;
        private Id<User, Long> userId;
        private Id<Post, Long> postId;
        private String contents;
        private Writer writer;
        private LocalDateTime createAt;

        public Builder() {}

        public Builder(Comment comment) {
            this.seq = comment.seq;
            this.userId = comment.userId;
            this.postId = comment.postId;
            this.contents = comment.contents;
            this.writer = comment.writer;
            this.createAt = comment.createAt;
        }

        public Builder seq(Long seq) {
            this.seq = seq;
            return this;
        }

        public Builder userId(Id<User, Long> userId) {
            this.userId = userId;
            return this;
        }

        public Builder postId(Id<Post, Long> postId) {
            this.postId = postId;
            return this;
        }

        public Builder contents(String contents) {
            this.contents = contents;
            return this;
        }

        public Builder writer(Writer writer) {
            this.writer = writer;
            return this;
        }

        public Builder createAt(LocalDateTime createAt) {
            this.createAt = createAt;
            return this;
        }

        public Comment build() {
            return new Comment(seq, userId, postId, contents, writer, createAt);
        }
    }
*/
}
