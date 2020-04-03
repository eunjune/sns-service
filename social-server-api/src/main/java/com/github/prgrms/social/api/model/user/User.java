package com.github.prgrms.social.api.model.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.prgrms.social.api.model.post.Post;
import com.github.prgrms.social.api.security.JWT;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.time.LocalDateTime.now;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@NoArgsConstructor(force = true)
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity(name = "users")
@ToString(exclude = {"followings","followers","posts"})
public class User {

    @ApiModelProperty(value = "PK", required = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;

    @ApiModelProperty(value = "이메일", required = true)
    @AttributeOverrides({
            @AttributeOverride(name = "address", column = @Column(name = "email", unique = true))
    })
    private final Email email;

    @ApiModelProperty(value = "사용자명", required = true)
    @Column(unique = true)
    private String name;

    @JsonIgnore
    @ApiModelProperty(value = "패스워드", hidden = true)
    @Column(nullable = false)
    private String password;

    @ApiModelProperty(value = "프로필 이미지 URL")
    private String profileImageUrl;

    @ApiModelProperty(value = "비공개 여부")
    private boolean isPrivate;

    @ApiModelProperty(value = "인증된 사용자")
    private boolean isEmailCertification;

    @ApiModelProperty(value = "인증 이메일 토큰")
    private String emailCertificationToken;

    @ApiModelProperty(value = "로그인 횟수", required = true)
    private int loginCount;

    @ApiModelProperty(value = "최종로그인일시")
    private LocalDateTime lastLoginAt;

    @ApiModelProperty(value = "생성일시", required = true)
    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime createAt;

    @ApiModelProperty(value = "팔로잉 목록")
    @ManyToMany(mappedBy = "followers")
    private Set<User> followings = new HashSet<>();

    @ApiModelProperty(value = "팔로워 목록")
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "connected_user",
            joinColumns = @JoinColumn(name = "following_id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id"))
    private Set<User> followers = new HashSet<>();

    @ApiModelProperty(value = "포스트 목록")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    @JsonBackReference
    private Set<Post> posts = new HashSet<>();

    @Builder(toBuilder = true)
    private User(Long id, String name, Email email, String password, boolean isEmailCertification, String emailCertificationToken, String profileImageUrl, int loginCount, LocalDateTime lastLoginAt, LocalDateTime createAt) {
        checkArgument(isNotEmpty(name), "name must be provided.");
        checkArgument(
                name.length() >= 1 && name.length() <= 10,
                "name length must be between 1 and 10 characters."
        );
        checkNotNull(email, "email must be provided.");
        checkNotNull(password, "password must be provided.");

        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.isEmailCertification = isEmailCertification;
        this.emailCertificationToken = emailCertificationToken;
        this.profileImageUrl = profileImageUrl;
        this.loginCount = loginCount;
        this.lastLoginAt = lastLoginAt;
        this.createAt = defaultIfNull(createAt, now());
    }

    public void afterLoginSuccess() {
        loginCount++;
        lastLoginAt = now();
    }

    public void newEmailToken() {
        this.emailCertificationToken = UUID.randomUUID().toString();
    }

    public String newApiToken(JWT jwt, String[] roles) {
        JWT.Claims claims = JWT.Claims.of(id, name, email, roles);
        return jwt.newToken(claims);
    }

    public void addFollowing(User targetUser) {
        followings.add(targetUser);
        targetUser.getFollowers().add(this);
    }

    public void removeFollowing(User targetUser) {
        followings.remove(targetUser);
        targetUser.removeFollower(this);
    }

    public void addPost(Post post) {
        this.posts.add(post);
        post.setUser(this);
    }

    public void removeFollower(User targetUser) {
        followers.remove(targetUser);
    }
}
