/**/
package com.github.prgrms.social.api.service.user;

import com.github.prgrms.social.api.model.user.ConnectedUser;
import com.github.prgrms.social.api.model.user.Email;
import com.github.prgrms.social.api.model.user.User;
import com.github.prgrms.social.api.repository.user.JpaConnectedUserRepository;
import com.github.prgrms.social.api.repository.user.JpaUserRepository;
import com.google.common.eventbus.EventBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JpaUserRepository userRepository;

    @Mock
    private JpaConnectedUserRepository connectedUserRepository;

    @Mock
    private EventBus eventBus;

    private String name;

    private Email email;

    private String password;

    @BeforeEach
    void setUp() {
        name = "test";
        email = new Email("test@gmail.com");
        password = "1234";
    }

    @DisplayName("사용자를 추가한다")
    @Test
    void join() {

        User givenUser = User.builder()
                .id(1L)
                .name(name)
                .email(email)
                .password(password)
                .build();
        given(passwordEncoder.encode(password)).willReturn("abcde");
        given(userRepository.save(User.builder().name(name).email(email).password(password).build())).willReturn(givenUser);

        userService.join(name, email, password, null);

        then(passwordEncoder).should(times(1)).encode(password);
        then(userRepository).should(times(1)).save(User.builder().name(name).email(email).password(password).build());
    }

    @DisplayName("사용자를 이메일로 조회한다")
    @Test
    void findByEmail() {
        User givenUser = User.builder()
                .id(1L)
                .name(name)
                .email(email)
                .password(password)
                .build();
        given(userRepository.findByEmail(email)).willReturn(Optional.ofNullable(givenUser));

        userService.findByEmail(email).orElse(null);

        then(userRepository).should(times(1)).findByEmail(email);
    }

    @DisplayName("이메일로 로그인한다")
    @Test
    void login() {

        int loginCount = 4;

        User givenUser = User.builder()
                .id(1L)
                .name(name)
                .email(email)
                .password(password)
                .loginCount(loginCount)
                .build();

        given(userRepository.findByEmail(email)) .willReturn(Optional.ofNullable(givenUser));
        given(passwordEncoder.matches(password, givenUser.getPassword())).willReturn(true);
        given(userRepository.save(givenUser)).willReturn(givenUser);

        userService.login(email, password);

        then(userRepository).should(times(1)).findByEmail(any());
        then(passwordEncoder).should(times(1)).matches(any(),any());
        then(userRepository).should(times(1)).save(any());

        assertEquals(givenUser.getLoginCount(), loginCount + 1);
    }

    @DisplayName("잘못된 비밀번호로 로그인을 할수없다")
    @Test
    void fail_password() {
        User givenUser = User.builder()
                .id(1L)
                .name(name)
                .email(email)
                .password(password)
                .build();

        given(userRepository.findByEmail(email)) .willReturn(Optional.ofNullable(givenUser));

        assertThrows(IllegalArgumentException.class, () -> userService.login(email, "invalid password"));
    }

    @DisplayName("친구 목록을 가져온다")
    @Test
    void find_connects() {
        User user1 = User.builder().id(1L).name(name).email(email).password(password).build();
        User user2 = User.builder().id(2L).name("test2").email(new Email("test2@gmail.com")).password(password).build();
        User user3 = User.builder().id(3L).name("test3").email(new Email("test2@gmail.com")).password(password).build();

        ConnectedUser connectedUser1 = new ConnectedUser(null, null);
        connectedUser1.setTargetUser(user2);
        user1.addConnectedUser(connectedUser1);

        ConnectedUser connectedUser2 = new ConnectedUser(null, null);
        connectedUser1.setTargetUser(user3);
        user1.addConnectedUser(connectedUser2);

        List<ConnectedUser> givenConnected = new ArrayList<>();
        givenConnected.add(connectedUser1);
        givenConnected.add(connectedUser2);

        given(connectedUserRepository.findByUser_IdAndCreateAtIsNotNullOrderByIdDesc(1L)).willReturn(givenConnected);

        connectedUserRepository.findByUser_IdAndCreateAtIsNotNullOrderByIdDesc(1L);

        then(connectedUserRepository).should(times(1)).findByUser_IdAndCreateAtIsNotNullOrderByIdDesc(any());
    }
}
