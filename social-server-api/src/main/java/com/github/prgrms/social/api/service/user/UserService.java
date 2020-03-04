package com.github.prgrms.social.api.service.user;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.github.prgrms.social.api.aws.S3Client;
import com.github.prgrms.social.api.error.NotFoundException;
import com.github.prgrms.social.api.event.JoinEvent;
import com.github.prgrms.social.api.model.commons.AttachedFile;
import com.github.prgrms.social.api.model.user.ConnectedUser;
import com.github.prgrms.social.api.model.user.Email;
import com.github.prgrms.social.api.model.user.User;
import com.github.prgrms.social.api.repository.post.projection.ConnectedId;
import com.github.prgrms.social.api.repository.user.JpaConnectedUserRepository;
import com.github.prgrms.social.api.repository.user.JpaUserRepository;
import com.google.common.eventbus.EventBus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Service
@Slf4j
public class UserService {

    private final S3Client s3Client;

    private final PasswordEncoder passwordEncoder;

    private final JpaUserRepository userRepository;

    private final JpaConnectedUserRepository connectedUserRepository;

    private final EventBus eventBus;

    public UserService(S3Client s3Client, PasswordEncoder passwordEncoder, JpaUserRepository userRepository, JpaConnectedUserRepository connectedUserRepository, EventBus eventBus) {
        this.s3Client = s3Client;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.connectedUserRepository = connectedUserRepository;
        this.eventBus = eventBus;
    }

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
    public List<ConnectedUser> findAllConnectedUser(Long userId) {
        checkNotNull(userId, "userId must be provided.");

        return connectedUserRepository.findByUser_IdAndCreateAtIsNotNullOrderByIdDesc(userId);
    }

    @Transactional(readOnly = true)
    public List<ConnectedId> findConnectedIds(Long userId) {
        checkNotNull(userId, "userId must be provided.");

        return connectedUserRepository.findByUser_IdAndCreateAtIsNotNullOrderByTargetUser_Id(userId);
    }

}
