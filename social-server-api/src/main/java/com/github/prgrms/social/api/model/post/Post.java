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
}
