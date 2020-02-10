package com.github.prgrms.social.api.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.prgrms.social.api.model.user.ConnectedUser;
import com.github.prgrms.social.api.model.user.Email;
import com.github.prgrms.social.api.model.user.Role;
import com.github.prgrms.social.api.model.user.User;
import com.github.prgrms.social.api.security.JWT;
import com.github.prgrms.social.api.service.user.UserService;
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
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private WebApplicationContext webApplicationContext;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    EntityManager entityManager;

    @MockBean
    UserService userService;
    @Value("${jwt.token.issuer}") String issuer;
    @Value("${jwt.token.clientSecret}") String clientSecret;
    @Value("${jwt.token.expirySeconds}") int expirySeconds;
    @Value("${jwt.token.header}") String tokenHeader;

    @Test
    void checkEmail() throws Exception {

        User user = User.builder().name("test").password("1234").email(new Email("test@gmail.com")).seq(1L).build();

        given(userService.findByEmail(new Email("test@gmail.com"))).willReturn(Optional.ofNullable(user));

        mockMvc.perform(post("/api/user/exists")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"address\" : \"test@gmail.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("response").value("true"))
                .andDo(print());

        then(userService).should().findByEmail(any());
    }


    @Test
    void join() throws Exception {

        JWT jwt = new JWT(issuer, clientSecret, expirySeconds);

        User user = User.builder().name("test").password("1234").email(new Email("test@gmail.com")).seq(1L).build();

        given(userService.join("test",new Email("test@gmail.com"), "1234", null)).willReturn(user);

        MockMultipartFile file = new MockMultipartFile("file", (byte[]) null);

        mockMvc.perform(multipart("/api/user/join")
                .file(file)
                .param("name","test").param("principal","test@gmail.com").param("credentials","1234"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.apiToken").value(user.newApiToken(jwt, new String[]{Role.USER.getValue()})))
                .andDo(print());

        then(userService).should().join(any(),any(),any(),any());
    }

    @Test
    void me() throws Exception {
        JWT jwt = new JWT(issuer, clientSecret, expirySeconds);

        User user = User.builder().name("test").password("1234").email(new Email("test@gmail.com")).seq(1L).build();
        String apiToken = "Bearer " + user.newApiToken(jwt, new String[]{Role.USER.getValue()});

        given(userService.findById(1L)).willReturn(Optional.ofNullable(user));

        mockMvc.perform(get("/api/user/me").header(tokenHeader,apiToken))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.response.seq").value(1L))
                        .andDo(print());

        then(userService).should(times(1)).findById(any());
    }

    @Test
    void connections() throws Exception {
        User user1 = User.builder().seq(1L).name("test1").email(new Email("test1@gmail.com")).password("1234").build();
        User user2 = User.builder().seq(2L).name("test2").email(new Email("test2@gmail.com")).password("1234").build();
        User user3 = User.builder().seq(3L).name("test3").email(new Email("test2@gmail.com")).password("1234").build();

        ConnectedUser connectedUser1 = new ConnectedUser(null, null);
        connectedUser1.setTargetUser(user2);
        user1.addConnectedUser(connectedUser1);

        ConnectedUser connectedUser2 = new ConnectedUser(null, null);
        connectedUser1.setTargetUser(user3);
        user1.addConnectedUser(connectedUser2);

        List<ConnectedUser> givenConnected = new ArrayList<>();
        givenConnected.add(connectedUser1);
        givenConnected.add(connectedUser2);

        JWT jwt = new JWT(issuer, clientSecret, expirySeconds);

        User user = User.builder().name("test1").password("1234").email(new Email("test1@gmail.com")).seq(1L).build();
        String apiToken = "Bearer " + user.newApiToken(jwt, new String[]{Role.USER.getValue()});

        given(userService.findAllConnectedUser(1L)).willReturn(givenConnected);

        mockMvc.perform(get("/api/user/connections").header(tokenHeader,apiToken))
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$.response.seq").value(1L))
                .andDo(print());

        then(userService).should(times(1)).findAllConnectedUser(any());
    }
}