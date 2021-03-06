package com.github.prgrms.social.api.service.user;

import com.github.prgrms.social.api.error.NotFoundException;
import com.github.prgrms.social.api.event.FollowEvent;
import com.github.prgrms.social.api.event.JoinEvent;
import com.github.prgrms.social.api.model.api.request.user.ProfileRequest;
import com.github.prgrms.social.api.model.user.Email;
import com.github.prgrms.social.api.model.user.Notification;
import com.github.prgrms.social.api.model.user.User;
import com.github.prgrms.social.api.repository.user.UserRepository;
import com.github.prgrms.social.api.service.FileService;
import com.github.prgrms.social.api.service.FileServiceLocal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    @Value("${app.default-user-image}")
    private String defaultUserImage;

    private final FileService fileService;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional(readOnly = true)
    public Optional<User> getUser(Long userId) {
        checkNotNull(userId, "userId must be provided.");

        return userRepository.findById(userId);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUser(Email email) {
        checkNotNull(email, "email must be provided.");

        return userRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithConnectedUserAndPost(Long userId) {
        checkNotNull(userId, "userId must be provided.");

        return userRepository.findUserWithUserWithPostById(userId);
    }

    @Transactional(readOnly = true)
    public List<User> getFollowings(Long userId, Pageable pageable) {
        checkNotNull(userId, "userId must be provided.");
        List<User> followings = userRepository.findFollowingsById(userId, pageable);
        return followings.size() == 1 && followings.get(0) == null ? new ArrayList<>() : followings;
    }

    @Transactional(readOnly = true)
    public List<User> getFollowers(Long userId, Pageable pageable) {
        checkNotNull(userId, "userId must be provided.");

        List<User> followers = userRepository.findFollowersById(userId, pageable);

        return followers.size() == 1 && followers.get(0) == null ? new ArrayList<>() : followers;
    }

    @Transactional
    public User join(String name, Email email, String password) {

        checkNotNull(email,"email must be provided.");
        checkArgument(isNotEmpty(name), "name must be provided.");
        checkArgument(isNotEmpty(password), "password must be provided.");
        checkArgument(
                password.length() >= 4 && password.length() <= 15,
                "password length must be between 4 and 15 characters."
        );

        User user = User.builder()
                .name(name)
                .email(email)
                .password(passwordEncoder.encode(password))
                .profileImageUrl(defaultUserImage)
                .build();

        User saved = userRepository.save(user);
        saved.newEmailToken();

        applicationEventPublisher.publishEvent(new JoinEvent(saved));

        return saved;
    }

    // 로그인 요청 처리
    @Transactional
    public User login(Email email, String password) {
        checkNotNull(email, "email must be provided.");
        checkArgument(isNotEmpty(password), "password must be provided.");

        return getUser(email)
                .map(user -> {

                    if (!passwordEncoder.matches(password, user.getPassword())){
                        throw new IllegalArgumentException("비밀번호가 틀립니다");
                    }

                    user.afterLoginSuccess();
                    return user;

        }).orElseThrow(() -> new NotFoundException(User.class, "이메일이 존재하지 않습니다"));
    }

    @Transactional
    public User certificateEmail(String token, String email) {
        checkArgument(isNotEmpty(token), "token must be provided.");
        checkArgument(isNotEmpty(email), "email must be provided.");

        return getUser(new Email(email))
                .map(user -> {
                    if(!user.getEmailCertificationToken().equals(token)) {
                        throw new IllegalArgumentException("유효하지 않은 접근 입니다.");
                    }

                    user.setEmailCertification(true);
                    return user;
                })
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 접근 입니다."));
    }

    @Transactional
    public User addFollowing(Long meId, Long userId) {
        checkNotNull(meId, "meId must be provided.");
        checkNotNull(userId, "userId must be provided.");

        User targetUser = getUser(userId).orElseThrow(() -> new NotFoundException(User.class, userId));

        return userRepository.findWithUserAllById(meId)
                .map(user -> {
                    user.addFollowing(targetUser);
                    applicationEventPublisher.publishEvent(new FollowEvent(user, targetUser));
                   return user;
                })
                .orElseThrow(() -> new NotFoundException(User.class, meId));
    }


    @Transactional
    public User updateProfile(Long id, ProfileRequest profileRequest){
        checkNotNull(id, "id must be provided.");
        checkNotNull(profileRequest, "profileRequest must be provided.");

        return getUser(id)
                .map(user -> {
                    profileRequest.encode(passwordEncoder);
                    modelMapper.map(profileRequest,user);
                    return user;
                })
                .orElseThrow(() -> new NotFoundException(User.class, id));
    }

    @Transactional
    public User updateProfileImage(Long id, MultipartFile file, String realPath) throws IOException {
        checkNotNull(id, "id must be provided.");
        checkNotNull(file, "file must be provided.");
        checkArgument(isNotEmpty(realPath),"realPath must be provided." );

        realPath = fileService instanceof FileServiceLocal ?
                realPath.substring(0,34) + "uploads" :
                null;

        String newProfileImageUrl = fileService.uploadFile(realPath, file);

        return getUser(id)
                .map(user -> {
                    user.setProfileImageUrl(newProfileImageUrl);
                    return user;
                })
                .orElseThrow(() -> new NotFoundException(User.class, id));
    }
    @Transactional
    public Long removeFollowing(Long meId, Long userId) {
        checkNotNull(meId, "meId must be provided.");
        checkNotNull(userId, "userId must be provided.");

        User targetUser = getUser(userId).orElseThrow(() -> new NotFoundException(User.class, userId));

        return userRepository.findWithUserAllById(meId)
                .map(user -> {
                    user.removeFollowing(targetUser);
                    return userId;
                })
                .orElseThrow(() -> new NotFoundException(User.class, meId));
    }

    @Transactional
    public Long removeFollower(Long meId, Long userId) {
        checkNotNull(meId, "meId must be provided.");
        checkNotNull(userId, "userId must be provided.");

        User me = getUser(meId).orElseThrow(() -> new NotFoundException(User.class, meId));

        return userRepository.findWithUserAllById(userId)
                .map(targetUser -> {
                    targetUser.removeFollowing(me);
                    return userId;
                })
                .orElseThrow(() -> new NotFoundException(User.class, userId));
    }



}
