package com.github.prgrms.social.api.service.user;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.github.prgrms.social.api.aws.S3Client;
import com.github.prgrms.social.api.error.NotFoundException;
import com.github.prgrms.social.api.event.JoinEvent;
import com.github.prgrms.social.api.model.commons.AttachedFile;
import com.github.prgrms.social.api.model.user.ConnectedUser;
import com.github.prgrms.social.api.model.user.Email;
import com.github.prgrms.social.api.model.user.User;
import com.github.prgrms.social.api.repository.user.JpaConnectedUserRepository;
import com.github.prgrms.social.api.repository.user.JpaUserRepository;
import com.github.prgrms.social.api.repository.user.projection.ConnectedId;
import com.google.common.eventbus.EventBus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final S3Client s3Client;

    private final PasswordEncoder passwordEncoder;

    private final JpaUserRepository userRepository;

    private final JpaConnectedUserRepository connectedUserRepository;

    private final EventBus eventBus;

    // S3에 이미지 업로드
    private String uploadProfileImage(AttachedFile profileFile) {

        String profileImage = null;

        if(profileFile != null) {
            try {
                profileImage = s3Client.upload(profileFile.inputStream(), profileFile.length()
                        , profileFile.randomName("eunjun","png"), profileFile.getContentType(), null);
            } catch(AmazonS3Exception e) {
                log.warn("Amazon S3 error (key : {} ) {}", e.getMessage(), e);
            }
        }

        return profileImage;
    }

    @Transactional
    public User join(String name, Email email, String password, AttachedFile profileFile) {
        checkArgument(isNotEmpty(password), "password must be provided.");
        checkArgument(
                password.length() >= 4 && password.length() <= 15,
                "password length must be between 4 and 15 characters."
        );

        String profileImageUrl = uploadProfileImage(profileFile);
        // 이미지 업로드 성공/실패 결과에 관계없이 join 트랜잭션을 처리한다.
        User user = User.builder()
                .name(name)
                .email(email)


                .password(passwordEncoder.encode(password))
                .profileImageUrl(profileImageUrl)
                .build();

        User saved = userRepository.save(user);

        // raise event
        eventBus.post(new JoinEvent(saved));
        return saved;
    }

    // 로그인 요청 처리
    @Transactional
    public User login(Email email, String password) {
        checkNotNull(password, "password must be provided.");

        return userRepository.findByEmail(email)
                .map(user -> {

                    if (!passwordEncoder.matches(password, user.getPassword()))
                        throw new IllegalArgumentException("Bad credential");

                    user.afterLoginSuccess();
                    userRepository.save(user);

                    return user;

        }).orElseThrow(() -> new NotFoundException(User.class, email));
    }

    @Transactional(readOnly = true)
    public Optional<User> findById(Long userId) {
        checkNotNull(userId, "userId must be provided.");

        return userRepository.findById(userId);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(Email email) {
        checkNotNull(email, "email must be provided.");

        return userRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public List<ConnectedId> findConnectedIds(Long userId) {
        checkNotNull(userId, "userId must be provided.");

        return connectedUserRepository.findByUser_IdAndCreateAtIsNotNullOrderByTargetUser_Id(userId);
    }

    @Transactional
    public User addFollowing(Long meId, Long userId) {
        checkNotNull(meId, "meId must be provided.");
        checkNotNull(userId, "userId must be provided.");

        User targetUser = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(User.class, meId));

        return userRepository.findById(meId)
                .map(user -> {
                    ConnectedUser connectedUser = new ConnectedUser(null,null);
                    connectedUser.setTargetUser(targetUser);
                    user.addConnectedUser(connectedUser);

                    return connectedUserRepository.save(connectedUser).getUser();
                })
                .orElseThrow(() -> new NotFoundException(User.class, meId));
    }

    @Transactional
    public Long removeFollowing(Long meId, Long userId) {
        checkNotNull(meId, "meId must be provided.");
        checkNotNull(userId, "userId must be provided.");


        return userRepository.findById(meId)
                .map(user -> {
                    List<ConnectedUser> connectedUsers = user.getConnectedUsers();
                    user.setConnectedUsers(new ArrayList<>());

                    for(ConnectedUser connectedUser : connectedUsers) {
                        if(!connectedUser.getTargetUser().getId().equals(userId)) {
                            user.addConnectedUser(connectedUser);
                        }
                    }

                    connectedUserRepository.deleteByUser_IdAndTargetUser_Id(meId, userId);

                    return userId;
                })
                .orElseThrow(() -> new NotFoundException(User.class, meId));
    }

    public List<User> getFollowings(Long id, Pageable pageable) {
        checkNotNull(id, "id must be provided.");

        List<ConnectedUser> result =  connectedUserRepository.findByUser_IdAndCreateAtIsNotNullOrderByIdDesc(id,pageable);
        return result.stream().map(ConnectedUser::getTargetUser).collect(Collectors.toList());
    }

    public List<User> getFollowers(Long id, Pageable pageable) {
        checkNotNull(id, "id must be provided.");

        List<ConnectedUser> result = connectedUserRepository.findByTargetUser_IdAndCreateAtIsNotNullOrderByIdDesc(id,pageable);
        return result.stream().map(ConnectedUser::getUser).collect(Collectors.toList());
    }

    public Long removeFollower(Long meId, Long userId) {
        checkNotNull(meId, "meId must be provided.");
        checkNotNull(userId, "userId must be provided.");

        return userRepository.findById(userId)
                .map(user -> {
                    List<ConnectedUser> connectedUsers = user.getConnectedUsers();
                    user.setConnectedUsers(new ArrayList<>());

                    for(ConnectedUser connectedUser : connectedUsers) {
                        if(!connectedUser.getTargetUser().getId().equals(meId)) {
                            user.addConnectedUser(connectedUser);
                        }
                    }

                    connectedUserRepository.deleteByUser_IdAndTargetUser_Id(userId, meId);

                    return userId;
                })
                .orElseThrow(() -> new NotFoundException(User.class, userId));
    }

    public User updateName(Long id, String name) {

        return userRepository.findById(id)
                .map(user -> {
                    User newUser =user.toBuilder().name(name).build();
                    return userRepository.save(newUser);
                })
                .orElseThrow(() -> new NotFoundException(User.class, id));
    }
}
