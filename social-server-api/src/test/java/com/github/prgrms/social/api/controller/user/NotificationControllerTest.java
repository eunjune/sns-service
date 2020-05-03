package com.github.prgrms.social.api.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.prgrms.social.api.model.api.request.post.CommentRequest;
import com.github.prgrms.social.api.model.post.Comment;
import com.github.prgrms.social.api.model.post.LikeInfo;
import com.github.prgrms.social.api.model.post.Post;
import com.github.prgrms.social.api.model.user.Email;
import com.github.prgrms.social.api.model.user.Notification;
import com.github.prgrms.social.api.model.user.Role;
import com.github.prgrms.social.api.model.user.User;
import com.github.prgrms.social.api.repository.post.CommentRepository;
import com.github.prgrms.social.api.repository.post.PostLikeRepository;
import com.github.prgrms.social.api.repository.post.PostRepository;
import com.github.prgrms.social.api.repository.user.NotificationRepository;
import com.github.prgrms.social.api.repository.user.UserRepository;
import com.github.prgrms.social.api.security.JWT;
import com.github.prgrms.social.api.service.post.CommentService;
import com.github.prgrms.social.api.service.post.PostService;
import com.github.prgrms.social.api.service.user.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class NotificationControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserService userService;

    @Autowired
    PostService postService;

    @Autowired
    CommentService commentService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    PostLikeRepository postLikeRepository;

    @Autowired
    NotificationRepository notificationRepository;

    @Value("${jwt.token.issuer}") String issuer;

    @Value("${jwt.token.clientSecret}") String clientSecret;

    @Value("${jwt.token.expirySeconds}") int expirySeconds;

    @Value("${jwt.token.header}") String tokenHeader;

    User user1;

    User user2;

    String apiToken;

    @BeforeEach
    void setup() {
        JWT jwt = new JWT(issuer, clientSecret, expirySeconds);
        user1 = userService.join("test1",new Email("test1@gmail.com"),"12345678");
        user2 = userService.join("test2",new Email("test2@gmail.com"),"12345678");
        userService.certificateEmail(user1.getEmailCertificationToken(), user1.getEmail().getAddress());
        userService.certificateEmail(user2.getEmailCertificationToken(), user2.getEmail().getAddress());
        apiToken = "Bearer " + this.user1.newApiToken(jwt, new String[]{Role.USER.getValue()});

        Post post1 = Post.builder().content("post1").build();
        Post savedPost = postService.write(post1, user1.getId(), new HashSet<>());
        postService.like(post1.getId(),user2.getId(),user1.getId());

        Comment comment1 = Comment.builder().content("comment1").build();
        commentService.write(savedPost.getId(), user2.getId(), user1.getId(), comment1);

        postService.retweet(savedPost.getId(),user2.getId());

        userService.addFollowing(user2.getId(), user1.getId());
    }

    @AfterEach
    void after() {
        notificationRepository.deleteAll();
        commentRepository.deleteAll();
        postLikeRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("알림 정보를 가져온다")
    void getNotification() throws Exception {
        mockMvc.perform(get("/api/user/notification")
                .header(tokenHeader,apiToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response.size()").value(4))
                .andDo(print());

    }

    @Test
    @DisplayName("알림 읽음")
    void read() throws Exception {

        mockMvc.perform(patch("/api/user/notification/1")
                .header(tokenHeader,apiToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response.readMark").value(true))
                .andDo(print());
    }

    @Test
    @DisplayName("알림 삭제")
    void removeNotification() throws Exception {
        mockMvc.perform(delete("/api/user/notification/1")
                .header(tokenHeader,apiToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response").value(1))
                .andDo(print());

        List<Notification> list = notificationRepository.findAll();
        assertEquals(3, list.size());
    }
}
