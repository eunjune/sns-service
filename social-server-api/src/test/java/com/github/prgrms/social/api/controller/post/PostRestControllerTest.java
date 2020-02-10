package com.github.prgrms.social.api.controller.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.prgrms.social.api.model.post.Comment;
import com.github.prgrms.social.api.model.post.Post;
import com.github.prgrms.social.api.model.user.Email;
import com.github.prgrms.social.api.model.user.Role;
import com.github.prgrms.social.api.model.user.User;
import com.github.prgrms.social.api.security.JWT;
import com.github.prgrms.social.api.service.post.CommentService;
import com.github.prgrms.social.api.service.post.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.hasSize;
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
class PostRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    PostService postService;

    @MockBean
    CommentService commentService;

    @Value("${jwt.token.issuer}") String issuer;
    @Value("${jwt.token.clientSecret}") String clientSecret;
    @Value("${jwt.token.expirySeconds}") int expirySeconds;
    @Value("${jwt.token.header}") String tokenHeader;

    User user;

    String apiToken;

    @BeforeEach
    void setup() {
        JWT jwt = new JWT(issuer, clientSecret, expirySeconds);
        user = User.builder().name("test").password("1234").email(new Email("test@gmail.com")).seq(1L).build();
        apiToken = "Bearer " + user.newApiToken(jwt, new String[]{Role.USER.getValue()});
    }


    @Test
    void posting() throws Exception {
        String content = randomAlphabetic(40);

        Post post = Post.builder().seq(1L).contents(content).build();

        given(postService.write(Post.builder().contents(content).build(),1L)).willReturn(post);

        mockMvc.perform(post("/api/post")
                .header(tokenHeader,apiToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"contents\" : \"" + content + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.seq").value(1L))
                .andDo(print());

        then(postService).should(times(1)).write(any(), any());

    }

    @Test
    void posts() throws Exception {
        String content1 = randomAlphabetic(40);
        String content2 = randomAlphabetic(40);

        Post post1 = Post.builder().seq(1L).contents(content1).build();
        Post post2 = Post.builder().seq(1L).contents(content2).build();

        List<Post> posts = new ArrayList<>();
        posts.add(post1);
        posts.add(post2);

        int page = 0;
        int size = 2;

        given(postService.findAll(1L, 1L, PageRequest.of(page,size))).willReturn(posts.subList(page,page + size));


        mockMvc.perform(get("/api/user/1/post/list")
                .header(tokenHeader, apiToken)
                .param("page", String.valueOf(page)).param("size",String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.*", hasSize(size)))
                .andDo(print());

        then(postService).should(times(1)).findAll(any(),any(),any());
    }

    @Test
    void like() throws Exception {
        String content = randomAlphabetic(40);

        Post post = Post.builder().seq(1L).contents(content).build();

        given(postService.like(1L,1L,1L)).willReturn(Optional.ofNullable(post));

        mockMvc.perform(patch("/api/user/1/post/1/like")
                        .header(tokenHeader,apiToken))
                        .andDo(print());

        then(postService).should(times(1)).like(any(),any(),any());

    }

    @Test
    void comment() throws Exception {
        String content = randomAlphabetic(40);
        Comment comment = Comment.builder().seq(1L).contents(content).build();

        given(commentService.write(1L, 1L, 1L, Comment.builder().contents(content).build()))
                .willReturn(comment);

        mockMvc.perform(post("/api/user/1/post/1/comment")
                        .header(tokenHeader, apiToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"contents\" : \"" + content + "\"}"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.response.seq").value(1L))
                        .andDo(print());

        then(commentService).should(times(1)).write(any(),any(),any(),any());
    }

    @Test
    void comments() throws Exception {
        String content1 = randomAlphabetic(40);
        String content2 = randomAlphabetic(40);

        Comment comment1 = Comment.builder().seq(1L).contents(content1).build();
        Comment comment2 = Comment.builder().seq(2L).contents(content2).build();

        List<Comment> comments = new ArrayList<>();
        comments.add(comment1);
        comments.add(comment2);

        given(commentService.findAll(1L,1L,1L)).willReturn(comments);

        mockMvc.perform(get("/api/user/1/post/1/comment/list")
                .header(tokenHeader, apiToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.*",hasSize(2)))
                .andDo(print());

        then(commentService).should(times(1)).findAll(any(),any(),any());
    }
}