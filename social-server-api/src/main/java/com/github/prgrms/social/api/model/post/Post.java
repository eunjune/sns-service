package com.github.prgrms.social.api.model.post;

import com.github.prgrms.social.api.model.user.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkArgument;
import static java.time.LocalDateTime.now;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Entity
@Getter
@Setter
@NoArgsConstructor(force = true)
@ToString(exclude = {"retweetPost","postsRetweetedMe","images","likeInfos"})
@EqualsAndHashCode(of = "id")
public class Post {

    @ApiModelProperty(value = "PK", required = true)
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;

    @ApiModelProperty(value = "내용", required = true)
    @Column(nullable = false)
    @Lob
    private String content;

    @ApiModelProperty(value = "좋아요 여부", required = true)
    @Transient
    private boolean likesOfMe;

    @ApiModelProperty(value = "리트윗 여부", required = true)
    private boolean isRetweet;

    @ApiModelProperty(value = "작성일시", required = true)
    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private final LocalDateTime createAt;

    // postCards.js(id,name), FollowButton.js(id)
    @ApiModelProperty(value = "작성자")
    @ManyToOne
    private User user;

    // postCards.js
    @ApiModelProperty(value = "이미지 리스트")
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private Set<Image> images = new HashSet<>();

    @ApiModelProperty(value = "댓글 리스트")
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private Set<Comment> comments = new HashSet<>();

    // postCards.js
    @ApiModelProperty(value = "좋아요 리스트")
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @Setter
    private Set<LikeInfo> likeInfos = new HashSet<>();

    // postCards.js(user, 나머지)
    @ApiModelProperty(value = "리트윗한 포스트")
    @ManyToOne
    @JoinTable(name = "retweet",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "target_post_id"))
    private Post retweetPost;

    @ApiModelProperty(value = "내 글에 리트윗한 포스트 목록")
    @OneToMany(mappedBy = "retweetPost",cascade = CascadeType.ALL)
    private Set<Post> postsRetweetedMe = new HashSet<>();

    //postCards.js
    // comments


    @Builder
    private Post(Long id, String content, boolean likesOfMe,boolean isRetweet, LocalDateTime createAt) {
        checkArgument(isNotEmpty(content), "contents must be provided.");
        checkArgument(
                content.length() >= 4 && content.length() <= 500,
                "post contents length must be between 4 and 500 characters."
        );

        this.id = id;
        this.content = content;
        this.likesOfMe = likesOfMe;
        this.isRetweet = isRetweet;
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

    public void setLikesOfMe(Long userId) {
        for(LikeInfo likeInfo : likeInfos) {
            if(likeInfo.getUser().getId().equals(userId)) {
                this.likesOfMe = true;
                return;
            }
        }
        this.likesOfMe = false;
    }

    public void incrementAndGetLikes(LikeInfo likeInfo) {
        likesOfMe = true;
        this.likeInfos.add(likeInfo);
        likeInfo.setPost(this);
    }

    public void removeLikes(LikeInfo likeInfo) {
        likesOfMe = false;
        this.likeInfos.remove(likeInfo);
        likeInfo.setPost(null);
    }

    public void removeImage(Image image) {
        this.images.remove(image);
        image.setPost(null);
    }

    public void addRetweet(Post post) {
        retweetPost = post;
        post.getPostsRetweetedMe().add(this);
    }

    public void addImage(Image image) {
        this.images.add(image);
        image.setPost(this);
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.setPost(this);
    }

/*
    public void setIsRetweet() {
        this.isRetweet = this.retweet != null;
    }*/

    public List<HashTag> findHashTag() {
        Pattern pattern = Pattern.compile("#[^\\s!@#$%^&*()+-=`~.;'\"?<>,./]+");
        Matcher matcher = pattern.matcher(content);
        List<HashTag> hashTags = new ArrayList<>();

        while(matcher.find()) {
            hashTags.add(HashTag.builder().name(matcher.group().substring(1)).build());
        }

        return hashTags;
    }
/*
    public void addHashTag(HashTag hashTag) {
        this.hashTags.add(hashTag);
        hashTag.getPosts().add(this);
    }

    public void addImage(Image image) {
        this.images.add(image);
        image.setPost(this);
    }*/
}
