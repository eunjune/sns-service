package com.github.prgrms.social.api.model.post;

import com.github.prgrms.social.api.model.user.Likes;
import com.github.prgrms.social.api.model.user.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static java.time.LocalDateTime.now;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Entity
@Getter
@NoArgsConstructor(force = true)
@ToString(exclude = {"user","likeList","commentList"})
@EqualsAndHashCode(of = "seq")
public class Post {

    @ApiModelProperty(value = "PK", required = true)
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long seq;

/*    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @AttributeOverride(name = "value", column = @Column(name="user_seq", nullable = false))
    private final Id<User, Long> userId;*/

    @ApiModelProperty(value = "내용", required = true)
    @Column(nullable = false)
    private String contents;

    @ApiModelProperty(value = "나의 좋아요 여부", required = true)
    @Transient
    private boolean likesOfMe; // like의 user_seq = login의 user_seq

    @ApiModelProperty(value = "작성일시", required = true)
    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private final LocalDateTime createAt;

    @ApiModelProperty(value = "작성자")
    @ManyToOne
    @Setter
    private User user;

    @ApiModelProperty(value = "좋아요 리스트", required = true)
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Likes> likeList = new ArrayList<>();

    @ApiModelProperty(value = "댓글 리스트", required = true)
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();

    @Builder
    private Post(Long seq, String contents, boolean likesOfMe, LocalDateTime createAt) {
        checkArgument(isNotEmpty(contents), "contents must be provided.");
        checkArgument(
                contents.length() >= 4 && contents.length() <= 500,
                "post contents length must be between 4 and 500 characters."
        );

        this.seq = seq;
        this.contents = contents;
        this.likesOfMe = likesOfMe;
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

    public void incrementAndGetLikes(Likes likes) {
        likesOfMe = true;
        this.getLikeList().add(likes);
        likes.setPost(this);
    }

    public void incrementAndGetComments(Comment comment) {
        this.getCommentList().add(comment);
        comment.setPost(this);
    }

    public void setLikesOfMe(Long userSeq) {
        for(Likes likes : likeList) {
            if(likes.getUser().getSeq().equals(userSeq)) {
                this.likesOfMe = true;
                return;
            }
        }
        this.likesOfMe = false;
    }

    /*
    public Post(Id<User, Long> userId, Writer writer, String contents) {
        this(null, userId, contents, 0, false, 0, writer, null);
    }


    public Long getSeq() {
        return seq;
    }

    public Id<User, Long> getUserId() {
        return userId;
    }

    public String getContents() {
        return contents;
    }

    public int getLikes() {
        return likes;
    }

    public boolean isLikesOfMe() {
        return likesOfMe;
    }

    public int getComments() {
        return comments;
    }

    public Optional<Writer> getWriter() {
        return ofNullable(writer);
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(seq, post.seq);
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
                .append("contents", contents)
                .append("likes", likes)
                .append("likesOfMe", likesOfMe)
                .append("comments", comments)
                .append("writer", writer)
                .append("createAt", createAt)
                .toString();
    }

    static public class Builder {
        private Long seq;
        private Id<User, Long> userId;
        private String contents;
        private int likes;
        private boolean likesOfMe;
        private int comments;
        private Writer writer;
        private LocalDateTime createAt;

        public Builder() {}

        public Builder(Post post) {
            this.seq = post.seq;
            this.userId = post.userId;
            this.contents = post.contents;
            this.likes = post.likes;
            this.likesOfMe = post.likesOfMe;
            this.comments = post.comments;
            this.writer = post.writer;
            this.createAt = post.createAt;
        }

        public Builder seq(Long seq) {
            this.seq = seq;
            return this;
        }

        public Builder userId(Id<User, Long> userId) {
            this.userId = userId;
            return this;
        }

        public Builder contents(String contents) {
            this.contents = contents;
            return this;
        }

        public Builder likes(int likes) {
            this.likes = likes;
            return this;
        }

        public Builder likesOfMe(boolean likesOfMe) {
            this.likesOfMe = likesOfMe;
            return this;
        }

        public Builder comments(int comments) {
            this.comments = comments;
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

        public Post build() {
            return new Post(seq, userId, contents, likes, likesOfMe, comments, writer, createAt);
        }
    }*/

}
