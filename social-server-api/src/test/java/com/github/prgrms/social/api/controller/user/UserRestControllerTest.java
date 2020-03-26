package com.github.prgrms.social.api.controller.user;

import com.github.prgrms.social.api.model.user.Email;
import com.github.prgrms.social.api.model.user.Role;
import com.github.prgrms.social.api.model.user.User;
import com.github.prgrms.social.api.repository.user.JpaUserRepository;
import com.github.prgrms.social.api.security.JWT;
import com.github.prgrms.social.api.service.user.EmailService;
import com.github.prgrms.social.api.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class UserRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserService userService;

    @MockBean
    EmailService emailService;

    @Autowired
    JpaUserRepository userRepository;

    @Value("${jwt.token.issuer}")
    String issuer;
    @Value("${jwt.token.clientSecret}")
    String clientSecret;
    @Value("${jwt.token.expirySeconds}")
    int expirySeconds;
    @Value("${jwt.token.header}")
    String tokenHeader;

    User user;

    String apiToken;

    @BeforeEach
    void setup() {
        JWT jwt = new JWT(issuer, clientSecret, expirySeconds);
        user = User.builder().name("test").password("12345678").email(new Email("test@gmail.com")).id(1L).build();
        apiToken = "Bearer " + user.newApiToken(jwt, new String[]{Role.USER.getValue()});
    }

    @DisplayName("이메일 확인 성공")
    @Test
    void checkEmail() throws Exception {

        mockMvc.perform(post("/api/user/exists/email")
                .param("address", "test@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("response").value("true"))
                .andDo(print());
    }

    @DisplayName("이메일 확인 실패 - 이메일 형식 아님")
    @Test
    void checkEmailFail() throws Exception {

        mockMvc.perform(post("/api/user/exists/email")
                .param("address", "test"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("success").value("false"))
                .andDo(print());


    }

    @DisplayName("이메일 확인 실패 - 이메일 중복")
    @Test
    void checkEmailFailOverlap() throws Exception {

        userService.join("name", new Email("test@gmail.com"), "12345678", null);

        mockMvc.perform(post("/api/user/exists/email")
                .param("address", "test@gmail.com"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("success").value("false"))
                .andDo(print());
    }

    @DisplayName("이름 확인 성공")
    @Test
    void checkName() throws Exception {

        mockMvc.perform(post("/api/user/exists/name")
                .param("name", "test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("response").value("true"))
                .andDo(print());
    }

    @DisplayName("이름 확인 실패")
    @Test
    void checkNameFail() throws Exception {

        userService.join("name", new Email("test@gmail.com"), "12345678", null);

        mockMvc.perform(post("/api/user/exists/name")
                .param("name", "test"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("success").value("false"))
                .andDo(print());
    }

    @DisplayName("회원가입 성공")
    @Test
    void join() throws Exception {

        MockMultipartFile file = new MockMultipartFile("file", (byte[]) null);

        mockMvc.perform(multipart("/api/user/join")
                .file(file)
                .param("name", "test").param("address", "test@gmail.com").param("password", "12345678"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(print());

        User user = userService.findByEmail(new Email("test@gmail.com")).orElse(null);
        assertNotNull(user);
        assertNotEquals(user.getPassword(), "12345678");
        assertNotNull(user.getEmailCertificationToken());
        then(emailService).should().sendEmailCertificationMessage(any(User.class));
    }

    @DisplayName("회원가입 실패")
    @Test
    void joinFail() throws Exception {

        MockMultipartFile file = new MockMultipartFile("file", (byte[]) null);

        mockMvc.perform(multipart("/api/user/join")
                .file(file)
                .param("name", "test").param("address", "test").param("password", "12345678"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andDo(print());

    }

    @DisplayName("인증 이메일 재전송")
    @Test
    void resendEmail() throws Exception {
        userService.join("name", new Email("test@gmail.com"), "12345678", null);

        mockMvc.perform(get("/api/user/resend-email")
                .header(tokenHeader, apiToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(print());

        then(emailService).should().sendEmailCertificationMessage(any(User.class));
    }

    @DisplayName("이메일 인증 확인 - 성공")
    @Test
    void checkEmailToken() throws Exception {
        User user = User.builder().name("test").password("12345678").email(new Email("test@gmail.com")).id(1L).build();
        user.newEmailToken();

        User savedUser = userRepository.save(user);

        mockMvc.perform(get("/api/auth/check-email-token")
                .param("email", "test@gmail.com")
                .param("token", savedUser.getEmailCertificationToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.isEmailCertification").value(true))
                .andDo(print());

    }

    @DisplayName("이메일 인증 확인 - 실패")
    @Transactional
    @Test
    void checkEmailTokenFail() throws Exception {
        User user = User.builder().name("test").password("12345678").email(new Email("test@gmail.com")).id(1L).build();

        User savedUser = userRepository.save(user);
        savedUser.newEmailToken();

        mockMvc.perform(get("/api/auth/check-email-token")
                .param("email", "test@gmail.com")
                .param("token", "afasfasd"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andDo(print());
    }

    @DisplayName("유저 정보를 가져온다")
    @Test
    void me() throws Exception {
        User user = User.builder().name("test").password("12345678").email(new Email("test@gmail.com")).id(1L).build();
        JWT jwt = new JWT(issuer, clientSecret, expirySeconds);
        String apiToken = "Bearer " + user.newApiToken(jwt, new String[]{Role.USER.getValue()});

        userService.join("name", new Email("test@gmail.com"), "12345678", null);

        mockMvc.perform(get("/api/user/me").header(tokenHeader, apiToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.id").value(1L))
                .andDo(print());

    }


    @DisplayName("로그인 성공")
    @Test
    void login() throws Exception {
        userService.join("name",new Email("test@gmail.com"),"12345678", null);

        mockMvc.perform(post("/api/auth")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"address\" : \"test@gmail.com\", \"password\" : \"12345678\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.token").isNotEmpty())
                .andExpect(jsonPath("$.response.user").isNotEmpty())
                .andDo(print());

    }

    @DisplayName("로그인 실패 - 이메일 존재하지 않음")
    @Test
    void loginFailEmail() throws Exception {

        mockMvc.perform(post("/api/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"address\" : \"test@gmail.com\", \"password\" : \"12345678\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value("false"))
                .andDo(print());

    }

    @DisplayName("로그인 실패 - 비밀번호 틀림")
    @Test
    void loginFailPassword() throws Exception {
        userService.join("name",new Email("test@gmail.com"),"12345678", null);

        mockMvc.perform(post("/api/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"address\" : \"test@gmail.com\", \"password\" : \"11111111\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value("false"))
                .andDo(print());

    }

    @DisplayName("이메일 로그인 성공")
    @Test
    void emailLogin() throws Exception {
        userService.join("name",new Email("test@gmail.com"),"12345678", null);

        mockMvc.perform(get("/api/auth/test@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.token").isNotEmpty())
                .andExpect(jsonPath("$.response.user").isNotEmpty())
                .andDo(print());

        then(emailService).should().sendEmailLoginLinkMessage(any(User.class), any());
    }

    @DisplayName("프로필 수정 - 성공")
    @Test
    void updateProfile() throws Exception {

        userService.join("test", new Email("test@gmail.com"), "12345678", null);
        User user = userRepository.findByName("test").orElse(null);

        String previousPassword = userRepository.findByName("test").orElse(null).getPassword();
        mockMvc.perform(put("/api/user/profile")
                        .header(tokenHeader, apiToken)
                        .param("name","testupdate")
                        .param("password","87654321"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.name").value("testupdate"))
                .andDo(print());


        User updatedUser = userRepository.findById(user.getId()).orElse(null);
        assertNotEquals(user.getName(), updatedUser.getName());
        assertNotEquals(user.getPassword(), updatedUser.getPassword());
    }

    @DisplayName("프로필 수정 - 실패")
    @Test
    void updateProfileFail() throws Exception {

        userService.join("test", new Email("test@gmail.com"), "12345678", null);

        mockMvc.perform(put("/api/user/profile")
                   .header(tokenHeader, apiToken)
                    .param("name","")
//                   .param("password","")
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}
