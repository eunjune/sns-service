package com.github.prgrms.social.api.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.prgrms.social.api.model.post.Comment;
import com.github.prgrms.social.api.model.post.Post;
import com.github.prgrms.social.api.security.JWT;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.time.LocalDateTime.now;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Entity(name = "users")
@Getter
@NoArgsConstructor(force = true)
@EqualsAndHashCode(of = "seq")
@ToString(exclude = {"connectedUser","posts","commentList"})
public class User {

    @ApiModelProperty(value = "PK", required = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long seq;

    @ApiModelProperty(value = "사용자명", required = true)
    @Column(nullable = false)
    private final String name;

    @ApiModelProperty(value = "이메일", required = true)
    @Column(nullable = false, unique = true)
    private final Email email;

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @Column(nullable = false)
    private String password;

    // TODO profileImageUrl 추가 (컬럼 profile_image_url varchar(255) 추가도 필요함)
    @ApiModelProperty(value = "프로필 이미지 URL")
    private String profileImageUrl;

    @ApiModelProperty(value = "로그인 횟수", required = true)
    @Column(nullable = false)
    private int loginCount;

    @ApiModelProperty(value = "최종로그인일시")
    private LocalDateTime lastLoginAt;

    @ApiModelProperty(value = "생성일시", required = true)
    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private final LocalDateTime createAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ConnectedUser> connectedUser = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Likes> likeList = new ArrayList<>();

    @Builder
    private User(Long seq, String name, Email email, String password, String profileImageUrl, int loginCount, LocalDateTime lastLoginAt, LocalDateTime createAt) {
        checkArgument(isNotEmpty(name), "name must be provided.");
        checkArgument(
                name.length() >= 1 && name.length() <= 10,
                "name length must be between 1 and 10 characters."
        );
        checkNotNull(email, "email must be provided.");
        checkNotNull(password, "password must be provided.");

        this.seq = seq;
        this.name = name;
        this.email = email;
        this.password = password;
        this.profileImageUrl = profileImageUrl;
        this.loginCount = loginCount;
        this.lastLoginAt = lastLoginAt;
        this.createAt = defaultIfNull(createAt, now());
    }

    public void afterLoginSuccess() {
        loginCount++;
        lastLoginAt = now();
    }

    public String newApiToken(JWT jwt, String[] roles) {
        JWT.Claims claims = JWT.Claims.of(seq, name, email, roles);
        return jwt.newToken(claims);
    }

    public Optional<String> getProfileImageUrl() {
        return ofNullable(profileImageUrl);
    }

    public Optional<LocalDateTime> getLastLoginAt() {
        return ofNullable(lastLoginAt);
    }

    public void addConnectedUser(ConnectedUser connectedUser) {
        this.getConnectedUser().add(connectedUser);
        connectedUser.setUser(this);
    }

    public void addPost(Post post) {
        this.getPosts().add(post);
        post.setUser(this);
    }

    public void addComment(Comment comment) {
        this.getCommentList().add(comment);
        comment.setUser(this);
    }

    public void addLike(Likes likes) {
        this.getLikeList().add(likes);
        likes.setUser(this);
    }
}
