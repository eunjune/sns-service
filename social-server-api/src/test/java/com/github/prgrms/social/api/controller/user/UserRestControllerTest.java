package com.github.prgrms.social.api.controller.user;

import com.github.prgrms.social.api.model.user.Email;
import com.github.prgrms.social.api.model.user.Role;
import com.github.prgrms.social.api.model.user.User;
import com.github.prgrms.social.api.security.JWT;
import com.github.prgrms.social.api.service.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
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

    @Value("${jwt.token.issuer}") String issuer;
    @Value("${jwt.token.clientSecret}") String clientSecret;
    @Value("${jwt.token.expirySeconds}") int expirySeconds;
    @Value("${jwt.token.header}") String tokenHeader;

    @DisplayName("이메일 확인 성공")
    @Test
    void checkEmail() throws Exception {

        mockMvc.perform(post("/api/user/exists/email")
                .param("address","test@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("response").value("true"))
                .andDo(print());
    }

    @DisplayName("이메일 확인 실패 - 이메일 형식 아님")
    @Test
    void checkEmailFail() throws Exception {

        mockMvc.perform(post("/api/user/exists/email")
                .param("address","test"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("success").value("false"))
                .andDo(print());


    }

    @DisplayName("이메일 확인 실패 - 이메일 중복")
    @Test
    void checkEmailFailOverlap() throws Exception {

        userService.join("name",new Email("test@gmail.com"),"12345678", null);

        mockMvc.perform(post("/api/user/exists/email")
                .param("address","test@gmail.com"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("success").value("false"))
                .andDo(print());
    }

    @DisplayName("이름 확인 성공")
    @Test
    void checkName() throws Exception {

        mockMvc.perform(post("/api/user/exists/name")
                .param("name","test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("response").value("true"))
                .andDo(print());
    }

    @DisplayName("이름 확인 실패")
    @Test
    void checkNameFail() throws Exception {

        userService.join("name",new Email("test@gmail.com"),"12345678", null);

        mockMvc.perform(post("/api/user/exists/name")
                .param("name","test"))
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
                .param("name","test").param("address","test@gmail.com").param("password","12345678"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(print());

        assertNotEquals(userService.findByEmail(new Email("test@gmail.com")).orElse(null).getPassword(), "12345678");
    }

    @DisplayName("회원가입 실패")
    @Test
    void joinFail() throws Exception {

        MockMultipartFile file = new MockMultipartFile("file", (byte[]) null);

        mockMvc.perform(multipart("/api/user/join")
                .file(file)
                .param("name","test").param("address","test").param("password","12345678"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andDo(print());

    }

    @Test
    void me() throws Exception {
        JWT jwt = new JWT(issuer, clientSecret, expirySeconds);

        User user = User.builder().name("test").password("1234").email(new Email("test@gmail.com")).id(1L).build();
        String apiToken = "Bearer " + user.newApiToken(jwt, new String[]{Role.USER.getValue()});

        given(userService.findById(1L)).willReturn(Optional.ofNullable(user));

        mockMvc.perform(get("/api/user/me").header(tokenHeader,apiToken))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.response.id()").value(1L))
                        .andDo(print());

        then(userService).should(times(1)).findById(any());
    }

}
