package com.github.prgrms.social.api.model.post;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.github.prgrms.social.api.model.user.Likes;
import com.github.prgrms.social.api.model.user.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkArgument;
import static java.time.LocalDateTime.now;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Entity
@Getter
@NoArgsConstructor(force = true)
@ToString(exclude = {"user","likes","comments", "images", "hashTags"})
@EqualsAndHashCode(of = "id")
public class Post {

    @ApiModelProperty(value = "PK", required = true)
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;

    @ApiModelProperty(value = "내용", required = true)
    @Column(nullable = false)
    private String content;

    @ApiModelProperty(value = "나의 좋아요 여부", required = true)
    @Transient
    private boolean likesOfMe; // like의 user_seq = login의 user_seq

    @ApiModelProperty(value = "작성일시", required = true)
    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private final LocalDateTime createAt;

    @ApiModelProperty(value = "작성자")
    @ManyToOne
    @Setter
    @JsonManagedReference
    private User user;

    @ApiModelProperty(value = "이미지 리스트")
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Image> images = new ArrayList<>();

    @ApiModelProperty(value = "좋아요 리스트")
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Likes> likes = new ArrayList<>();

    @ApiModelProperty(value = "댓글 리스트")
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Comment> comments = new ArrayList<>();

    @ApiModelProperty(value = "해쉬태그 리스트")
    @ManyToMany(mappedBy = "posts", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<HashTag> hashTags = new ArrayList<>();

    @Builder
    private Post(Long id, String content, boolean likesOfMe, LocalDateTime createAt) {
        checkArgument(isNotEmpty(content), "contents must be provided.");
        checkArgument(
                content.length() >= 4 && content.length() <= 500,
                "post contents length must be between 4 and 500 characters."
        );

        this.id = id;
        this.content = content;
        this.likesOfMe = likesOfMe;
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

    public void incrementAndGetLikes(Likes likes) {
        likesOfMe = true;
        this.likes.add(likes);
        likes.setPost(this);
    }

    public void incrementAndGetComments(Comment comment) {
        this.comments.add(comment);
        comment.setPost(this);
    }

    public void setLikesOfMe(Long userSeq) {
        for(Likes likes : likes) {
            if(likes.getUser().getId().equals(userSeq)) {
                this.likesOfMe = true;
                return;
            }
        }
        this.likesOfMe = false;
    }

    public List<HashTag> findHashTag() {
        Pattern pattern = Pattern.compile("#[^\\s!@#$%^&*()+-=`~.;'\"?<>,./]+");
        Matcher matcher = pattern.matcher(content);
        List<HashTag> hashTags = new ArrayList<>();

        while(matcher.find()) {
            hashTags.add(HashTag.builder().name(matcher.group().substring(1)).build());
        }

        return hashTags;
    }

    public void addHashTag(HashTag hashTag) {
        this.hashTags.add(hashTag);
        hashTag.getPosts().add(this);
    }

    public void addImage(Image image) {
        this.images.add(image);
        image.setPost(this);
    }
}
