package com.github.prgrms.social.api.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.prgrms.social.api.model.api.request.user.EmailAuthenticationRequest;
import com.github.prgrms.social.api.model.user.Email;
import com.github.prgrms.social.api.model.user.Role;
import com.github.prgrms.social.api.model.user.User;
import com.github.prgrms.social.api.repository.user.JpaUserRepository;
import com.github.prgrms.social.api.security.JWT;
import com.github.prgrms.social.api.service.user.EmailService;
import com.github.prgrms.social.api.service.user.UserService;
import org.junit.jupiter.api.AfterEach;
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

import static org.junit.jupiter.api.Assertions.*;
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
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserService userService;

    @MockBean
    EmailService emailService;

    @Autowired
    JpaUserRepository userRepository;

    @Value("${jwt.token.issuer}") String issuer;

    @Value("${jwt.token.clientSecret}") String clientSecret;

    @Value("${jwt.token.expirySeconds}") int expirySeconds;

    @Value("${jwt.token.header}") String tokenHeader;

    User user;

    String apiToken;

    @BeforeEach
    void setup() {
        JWT jwt = new JWT(issuer, clientSecret, expirySeconds);
        user = userService.join("test1",new Email("test1@gmail.com"),"12345678");
        apiToken = "Bearer " + this.user.newApiToken(jwt, new String[]{Role.USER.getValue()});
    }

    @AfterEach
    void after() {
        userRepository.deleteAll();
    }

    @DisplayName("이메일 확인 성공")
    @Test
    void checkEmail() throws Exception {

        mockMvc.perform(post("/api/user/exists/email")
                .param("address", "test2@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("response").value("true"))
                .andDo(print());
    }

    @DisplayName("이메일 확인 실패 - 이메일 형식 아님")
    @Test
    void checkEmailFail() throws Exception {

        mockMvc.perform(post("/api/user/exists/email")
                .param("address", "test2"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("success").value("false"))
                .andDo(print());


    }

    @DisplayName("이메일 확인 실패 - 이메일 중복")
    @Test
    void checkEmailFailOverlap() throws Exception {

        mockMvc.perform(post("/api/user/exists/email")
                .param("address", user.getEmail().getAddress()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("success").value("false"))
                .andDo(print());
    }

    @DisplayName("이름 확인 성공")
    @Test
    void checkName() throws Exception {
        mockMvc.perform(post("/api/user/exists/name")
                .param("name", "test2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("response").value("true"))
                .andDo(print());
    }

    @DisplayName("이름 확인 실패")
    @Test
    void checkNameFail() throws Exception {
        mockMvc.perform(post("/api/user/exists/name")
                .param("name", user.getName()))
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
                .param("name", "test2").param("address", "test2@gmail.com").param("password", "12345678"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(print());

        User user2 = userService.findByEmail(new Email("test2@gmail.com")).orElse(null);
        assertNotNull(user2);
        assertNotEquals(user2.getPassword(), "12345678");
        assertNotNull(user2.getEmailCertificationToken());
        then(emailService).should().sendEmailCertificationMessage(any(User.class));
    }

    @DisplayName("회원가입 실패")
    @Test
    void joinFail() throws Exception {

        MockMultipartFile file = new MockMultipartFile("file", (byte[]) null);

        mockMvc.perform(multipart("/api/user/join")
                .file(file)
                .param("name", "test2").param("address", "test2@").param("password", "12345678"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andDo(print());

    }

    @DisplayName("인증 이메일 재전송")
    @Test
    void resendEmail() throws Exception {
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
        EmailAuthenticationRequest emailAuthenticationRequest = new EmailAuthenticationRequest(user.getEmailCertificationToken(),user.getEmail().getAddress());

        mockMvc.perform(post("/api/check-email-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emailAuthenticationRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.isEmailCertification").value(true))
                .andDo(print());

    }

    @DisplayName("이메일 인증 확인 - 실패")
    @Test
    void checkEmailTokenFail() throws Exception {

        user.newEmailToken();

        EmailAuthenticationRequest emailAuthenticationRequest = new EmailAuthenticationRequest("asdfadsfadsf",user.getEmail().getAddress());

        mockMvc.perform(post("/api/check-email-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emailAuthenticationRequest)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @DisplayName("유저 정보를 가져온다")
    @Test
    void me() throws Exception {

        mockMvc.perform(get("/api/user/me").header(tokenHeader, apiToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.id").value(user.getId()))
                .andDo(print());

    }


    @DisplayName("로그인 성공")
    @Test
    void login() throws Exception {
        mockMvc.perform(post("/api/auth")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"address\" : \"test1@gmail.com\", \"password\" : \"12345678\"}"))
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
        mockMvc.perform(post("/api/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"address\" : \"test1@gmail.com\", \"password\" : \"11111111\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value("false"))
                .andDo(print());

    }

    @DisplayName("이메일 로그인 성공")
    @Test
    void emailLogin() throws Exception {
        mockMvc.perform(get("/api/auth/test1@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.token").isNotEmpty())
                .andExpect(jsonPath("$.response.user").isNotEmpty())
                .andDo(print());

        then(emailService).should().sendEmailLoginLinkMessage(any(User.class), any());
    }

    @DisplayName("프로필 수정 - 성공")
    @Test
    void updateProfile() throws Exception {

        mockMvc.perform(put("/api/user/profile")
                        .header(tokenHeader, apiToken)
                        .param("name","testupdate")
                        .param("password","87654321"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.name").value("testupdate"))
                .andDo(print());


        User updatedUser = userRepository.findById(user.getId()).orElse(null);
        assertNotNull(updatedUser);
        assertNotEquals(user.getName(), updatedUser.getName());
        assertNotEquals(user.getPassword(), updatedUser.getPassword());
    }

    @DisplayName("프로필 수정 - 실패")
    @Test
    void updateProfileFail() throws Exception {
        mockMvc.perform(put("/api/user/profile")
                   .header(tokenHeader, apiToken)
                    .param("name","")
//                   .param("password","")
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @DisplayName("팔로우 추가")
    @Test
    void follow() throws Exception{

        User user2 = userService.join("test2", new Email("test2@gmail.com"), "12345678");

        mockMvc.perform(post("/api/user/" + user2.getId() + "/follow")
                        .header(tokenHeader, apiToken))
                        .andExpect(status().isOk())
                        .andDo(print());

        User resultUser1 = userService.findUserWithUserById(user.getId()).orElse(null);
        User resultUser2 = userService.findUserWithUserById(user2.getId()).orElse(null);

        assertNotNull(resultUser1);
        assertNotNull(resultUser2);
        assertEquals(user.getFollowings().size() + 1, resultUser1.getFollowings().size());
        assertEquals(user2.getFollowers().size() + 1, resultUser2.getFollowers().size());
    }


    @DisplayName("언팔로우")
    @Test
    void unfollow() throws Exception {
        User user2 = userService.join("test2", new Email("test2@gmail.com"), "12345678");

        userService.addFollowing(user.getId(), user2.getId());

        User beforeUser1 = userService.findUserWithUserById(user.getId()).orElse(null);
        User beforeUser2 = userService.findUserWithUserById(user2.getId()).orElse(null);

        mockMvc.perform(delete("/api/user/" + user2.getId() + "/follow")
                .header(tokenHeader, apiToken))
                .andExpect(status().isOk())
                .andDo(print());

        User afterUser1 = userService.findUserWithUserById(user.getId()).orElse(null);
        User afterUser2 = userService.findUserWithUserById(user2.getId()).orElse(null);

        assertNotNull(beforeUser1);
        assertNotNull(beforeUser2);
        assertNotNull(afterUser1);
        assertNotNull(afterUser2);
        assertEquals(beforeUser1.getFollowings().size() - 1, afterUser1.getFollowings().size());
        assertEquals(beforeUser2.getFollowers().size() - 1, afterUser2.getFollowers().size());
    }

    @DisplayName("팔로워 삭제")
    @Test
    void removeFollower() throws Exception {

        User user2 = userService.join("test2", new Email("test2@gmail.com"), "12345678");

        JWT jwt = new JWT(issuer, clientSecret, expirySeconds);
        String user2apiToken = "Bearer " + user2.newApiToken(jwt, new String[]{Role.USER.getValue()});

        userService.addFollowing(user.getId(), user2.getId());

        User beforeUser1 = userService.findUserWithUserById(user.getId()).orElse(null);
        User beforeUser2 = userService.findUserWithUserById(user2.getId()).orElse(null);

        mockMvc.perform(delete("/api/user/" + user.getId() + "/follower")
                .header(tokenHeader, user2apiToken))
                .andExpect(status().isOk())
                .andDo(print());

        User afterUser1 = userService.findUserWithUserById(user.getId()).orElse(null);
        User afterUser2 = userService.findUserWithUserById(user2.getId()).orElse(null);

        assertNotNull(beforeUser1);
        assertNotNull(beforeUser2);
        assertNotNull(afterUser1);
        assertNotNull(afterUser2);
        assertEquals(beforeUser1.getFollowings().size() - 1, afterUser1.getFollowings().size());
        assertEquals(beforeUser2.getFollowers().size() - 1, afterUser2.getFollowers().size());


    }
}
