package com.github.prgrms.social.api.model.user;

import com.github.prgrms.social.api.model.BaseTimeEntity;
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
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@NoArgsConstructor(force = true)
@Getter
@Setter
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(exclude = {"followings","followers","posts","notifications"})
@Entity(name = "users")
public class User extends BaseTimeEntity{

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

    @ApiModelProperty(value = "패스워드", hidden = true)
    @Column(nullable = false)
    private String password;

    @ApiModelProperty(value = "프로필 이미지 URL")
    private String profileImageUrl;

    @ApiModelProperty(value = "비공개 여부", required = true)
    private boolean isPrivate;

    @ApiModelProperty(value = "인증된 사용자", required = true)
    private boolean isEmailCertification;

    @ApiModelProperty(value = "인증 이메일 토큰", required = true)
    private String emailCertificationToken;

    @ApiModelProperty(value = "로그인 횟수", required = true)
    private int loginCount;

    @ApiModelProperty(value = "최종 로그인 일시")
    private LocalDateTime lastLoginAt;


    // profile.js, FollowButton.js
    @ApiModelProperty(value = "팔로잉 목록")
    @ManyToMany(mappedBy = "followers")
    private Set<User> followings = new HashSet<>();

    // profile.js
    @ApiModelProperty(value = "팔로워 목록")
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "connected_user",
            joinColumns = @JoinColumn(name = "following_id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id"))
    private Set<User> followers = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Post> posts = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Notification> notifications = new HashSet<>();

    @Builder(toBuilder = true)
    private User(Long id, String name, Email email, String password, String profileImageUrl, boolean isPrivate, boolean isEmailCertification, String emailCertificationToken, int loginCount, LocalDateTime lastLoginAt) {
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
        this.profileImageUrl = profileImageUrl;
        this.isPrivate = isPrivate;
        this.isEmailCertification = isEmailCertification;
        this.emailCertificationToken = emailCertificationToken;
        this.loginCount = loginCount;
        this.lastLoginAt = lastLoginAt;
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
