package com.github.prgrms.social.api.controller.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.prgrms.social.api.model.post.Post;
import com.github.prgrms.social.api.model.user.Email;
import com.github.prgrms.social.api.model.user.Role;
import com.github.prgrms.social.api.model.user.User;
import com.github.prgrms.social.api.repository.post.*;
import com.github.prgrms.social.api.repository.user.UserRepository;
import com.github.prgrms.social.api.security.JWT;
import com.github.prgrms.social.api.service.post.CommentService;
import com.github.prgrms.social.api.service.post.PostService;
import com.github.prgrms.social.api.service.user.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class SearchControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

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
    HashTagRepository hashTagRepository;

    @Autowired
    ImageRepository imageRepository;

    @Value("${jwt.token.issuer}") String issuer;

    @Value("${jwt.token.clientSecret}") String clientSecret;

    @Value("${jwt.token.expirySeconds}") int expirySeconds;

    @Value("${jwt.token.header}") String tokenHeader;

    User user;

    String apiToken;

    @BeforeEach
    void setup() {
        JWT jwt = new JWT(issuer, clientSecret, expirySeconds);
        user = userService.join("test", new Email("test@gmail.com"), "12345678");
        apiToken = "Bearer " + user.newApiToken(jwt, new String[]{Role.USER.getValue()});
        userService.certificateEmail(user.getEmailCertificationToken(), user.getEmail().getAddress());
    }

    @AfterEach
    void after() {
        imageRepository.deleteAll();
        hashTagRepository.deleteAll();
        commentRepository.deleteAll();
        postLikeRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Test
    void search() throws Exception {
        Post post1 = Post.builder().content("post1 hashtag").build();
        Post post2 = Post.builder().content("hashtagpost2").build();
        Post post3 = Post.builder().content("hashtag post3").build();
        Post post4 = Post.builder().content("sdfdsfhashtagpost4").build();

        postService.write(post1,user.getId(),new HashSet<>());
        postService.write(post2,user.getId(),new HashSet<>());
        postService.write(post3,user.getId(),new HashSet<>());
        postService.write(post4, user.getId(), new HashSet<>());

        mockMvc.perform(get("/api/search/hashtag?lastId=2&size=2"))
                .andExpect(status().isOk())
                .andDo(print());

    }
}
