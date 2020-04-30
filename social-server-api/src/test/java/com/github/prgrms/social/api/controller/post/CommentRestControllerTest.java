package com.github.prgrms.social.api.controller.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.prgrms.social.api.model.api.request.post.CommentRequest;
import com.github.prgrms.social.api.model.post.Comment;
import com.github.prgrms.social.api.model.post.Post;
import com.github.prgrms.social.api.model.user.Email;
import com.github.prgrms.social.api.model.user.Notification;
import com.github.prgrms.social.api.model.user.Role;
import com.github.prgrms.social.api.model.user.User;
import com.github.prgrms.social.api.repository.post.*;
import com.github.prgrms.social.api.repository.user.NotificationRepository;
import com.github.prgrms.social.api.repository.user.UserRepository;
import com.github.prgrms.social.api.security.JWT;
import com.github.prgrms.social.api.service.post.CommentService;
import com.github.prgrms.social.api.service.post.PostService;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class CommentRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserService userService;

    @Autowired
    PostService postService;

    @MockBean
    EmailService emailService;

    @Autowired
    CommentService commentService;

    @Value("${jwt.token.issuer}") String issuer;

    @Value("${jwt.token.clientSecret}") String clientSecret;

    @Value("${jwt.token.expirySeconds}") int expirySeconds;

    @Value("${jwt.token.header}") String tokenHeader;

    User user;

    String apiToken;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    PostLikeRepository postLikeRepository;

    @Autowired
    HashTagRepository hashTagRepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    NotificationRepository notificationRepository;

    @BeforeEach
    void setup() {
        JWT jwt = new JWT(issuer, clientSecret, expirySeconds);
        user = userService.join("test", new Email("test@gmail.com"), "12345678");
        apiToken = "Bearer " + user.newApiToken(jwt, new String[]{Role.USER.getValue()});
    }

    @AfterEach
    void after() {
        imageRepository.deleteAll();
        hashTagRepository.deleteAll();
        commentRepository.deleteAll();
        postLikeRepository.deleteAll();
        postRepository.deleteAll();
        notificationRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void comments() {
    }

    @DisplayName("댓글 작성")
    @Test
    void comment() throws Exception {
        User user2 = userService.join("test2",new Email("test2@gmail.com"),"12345678");
        user2 = userService.certificateEmail(user2.getEmailCertificationToken(), user2.getEmail().getAddress());

        Post post1 = Post.builder().content("post1").build();
        Post savedPost = postService.write(post1, user2.getId(), new HashSet<>());
        CommentRequest commentRequest = new CommentRequest("comment1");

        mockMvc.perform(post("/api/user/" + user2.getId() + "/post/" + savedPost.getId() +"/comment")
                .header(tokenHeader,apiToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.content").value(commentRequest.getContent()))
                .andExpect(jsonPath("$.response.user").isNotEmpty())
                .andDo(print());

        List<Comment> comments = commentService.findAll(savedPost.getId(),user.getId(),null);
        assertNotNull(comments);
        assertEquals(comments.size(),1);

        List<Notification> notificationList = notificationRepository.findAll();
        assertEquals(1, notificationList.size());
    }
}
