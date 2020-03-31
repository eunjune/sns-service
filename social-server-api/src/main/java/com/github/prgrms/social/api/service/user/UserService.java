package com.github.prgrms.social.api.service.user;

import com.github.prgrms.social.api.aws.S3Client;
import com.github.prgrms.social.api.error.NotFoundException;
import com.github.prgrms.social.api.event.JoinEvent;
import com.github.prgrms.social.api.model.api.request.user.ProfileRequest;
import com.github.prgrms.social.api.model.commons.AttachedFile;
import com.github.prgrms.social.api.model.user.Email;
import com.github.prgrms.social.api.model.user.User;
import com.github.prgrms.social.api.repository.user.JpaUserRepository;
import com.google.common.eventbus.EventBus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

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

    private final ModelMapper modelMapper;

    private final EventBus eventBus;



    @Transactional
    public User join(String name, Email email, String password) {
        checkArgument(isNotEmpty(password), "password must be provided.");
        checkArgument(
                password.length() >= 4 && password.length() <= 15,
                "password length must be between 4 and 15 characters."
        );

        User user = User.builder()
                .name(name)
                .email(email)
                .password(passwordEncoder.encode(password))
                .build();

        User saved = userRepository.save(user);
        saved.newEmailToken();

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

                    if (!passwordEncoder.matches(password, user.getPassword())){
                        throw new IllegalArgumentException("비밀번호가 틀립니다");
                    }

                    user.afterLoginSuccess();
                    userRepository.save(user);

                    return user;

        }).orElseThrow(() -> new NotFoundException("이메일이 존재하지 않습니다"));
    }

    @Transactional
    public User login(Email email) {

        return userRepository.findByEmail(email)
                .map(user -> {

                    user.afterLoginSuccess();
                    userRepository.save(user);

                    return user;

                }).orElseThrow(() -> new NotFoundException("이메일이 존재하지 않습니다"));
    }

    @Transactional(readOnly = true)
    public Optional<User> findById(Long userId) {
        checkNotNull(userId, "userId must be provided.");

        return userRepository.findById(userId);
    }

    @Transactional(readOnly = true)
    public Optional<User> findUserWithUserById(Long userId) {
        checkNotNull(userId, "userId must be provided.");

        return userRepository.findUserWithUserById(userId);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(Email email) {
        checkNotNull(email, "email must be provided.");

        return userRepository.findByEmail(email);
    }

    @Transactional
    public User certificateEmail(String token, String email) {
        return findByEmail(new Email(email))
                .map(user -> {
                    System.out.println("토큰");
                    System.out.println(user.getEmailCertificationToken());
                    System.out.println(token);
                    if(!user.getEmailCertificationToken().equals(token)) {
                        throw new IllegalArgumentException("유효하지 않은 접근 입니다.");
                    }

                    user.setEmailCertification(true);
                    user.setCreateAt(LocalDateTime.now());
                    return user;
                })
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 접근 입니다."));
    }

    @Transactional
    public User addFollowing(Long meId, Long userId) {
        checkNotNull(meId, "meId must be provided.");
        checkNotNull(userId, "userId must be provided.");

        User targetUser = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(User.class, meId));

        return userRepository.findUserWithUserById(meId)
                .map(user -> {
                    user.addFollowing(targetUser);
                   return user;
                })
                .orElseThrow(() -> new NotFoundException(User.class, meId));
    }

    @Transactional
    public Long removeFollowing(Long meId, Long userId) {
        checkNotNull(meId, "meId must be provided.");
        checkNotNull(userId, "userId must be provided.");

        User targetUser = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(User.class, userId));

        return userRepository.findUserWithUserById(meId)
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

        User me = userRepository.findById(meId).orElseThrow(() -> new NotFoundException(User.class, meId));

        return userRepository.findUserWithUserById(userId)
                .map(targetUser -> {
                    targetUser.removeFollowing(me);
                    return userId;
                })
                .orElseThrow(() -> new NotFoundException(User.class, userId));
    }

    @Transactional
    public User updateProfile(Long id, ProfileRequest profileRequest){
        checkNotNull(profileRequest, "files must be provided.");

        return userRepository.findById(id)
                .map(user -> {
                    profileRequest.setPassword(passwordEncoder.encode(profileRequest.getPassword()));
                    modelMapper.map(profileRequest,user);
                    return user;
                })
                .orElseThrow(() -> new NotFoundException(User.class, id));
    }

    @Transactional
    public User updateProfileImage(Long id, MultipartFile file, String realPath) throws IOException {
        checkNotNull(file, "file must be provided.");

        //Todo 메소드 분리
        //Todo 배포시 변경 필요
        realPath = realPath.substring(0,34) + "uploads" + File.separator + "profile";
        AttachedFile attachedFile = AttachedFile.toAttachedFile(file);
        assert attachedFile != null;
        String extension = attachedFile.extension("png");
        String randomName = attachedFile.randomName(realPath,extension);
        file.transferTo(new File(randomName));
        String newProfileImageUrl = randomName.substring(realPath.length()+1);

        return userRepository.findById(id)
                .map(user -> {
                    user.setProfileImageUrl(newProfileImageUrl);
                    return user;
                })
                .orElseThrow(() -> new NotFoundException(User.class, id));
    }

    // S3에 이미지 업로드
    /*private String uploadProfileImage(AttachedFile profileFile) {

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
    }*/
}
