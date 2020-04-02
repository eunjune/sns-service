package com.github.prgrms.social.api.controller.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.prgrms.social.api.model.api.request.post.CommentRequest;
import com.github.prgrms.social.api.model.api.request.post.PostingRequest;
import com.github.prgrms.social.api.model.post.Comment;
import com.github.prgrms.social.api.model.post.HashTag;
import com.github.prgrms.social.api.model.post.Image;
import com.github.prgrms.social.api.model.post.Post;
import com.github.prgrms.social.api.model.user.Email;
import com.github.prgrms.social.api.model.user.Role;
import com.github.prgrms.social.api.model.user.User;
import com.github.prgrms.social.api.repository.post.*;
import com.github.prgrms.social.api.repository.user.JpaUserRepository;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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

    @Autowired
    UserService userService;

    @Autowired
    PostService postService;

    @Autowired
    CommentService commentService;

    @Autowired
    JpaUserRepository userRepository;

    @Autowired
    JpaPostRepository postRepository;

    @Autowired
    JpaCommentRepository commentRepository;

    @Autowired
    JpaPostLikeRepository postLikeRepository;

    @Autowired
    JpaHashTagRepository hashTagRepository;

    @Autowired
    JpaImageRepository imageRepository;

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


    @DisplayName("포스트 작성")
    @Test
    void posting() throws Exception {
        List<String> imagePaths = new ArrayList<>();
        imagePaths.add("image1.png");
        imagePaths.add("image2.png");
        String content = "content testsets #abc #hash sdfsdfsdfds";
        PostingRequest postingRequest = new PostingRequest(content,imagePaths);

        List<Post> beforePosts = postService.findAll(0L,PageRequest.of(0,4));
        List<HashTag> beforeHashTagList = hashTagRepository.findAll();
        List<Image> beforeImageList = imageRepository.findAll();


        mockMvc.perform(post("/api/post")
                .header(tokenHeader,apiToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postingRequest)))
                .andExpect(status().isOk())
                .andDo(print());

        List<Post> afterPosts = postService.findAll(0L,PageRequest.of(0,4));
        List<HashTag> afterHashTagList = hashTagRepository.findAll();
        List<Image> afterImageList = imageRepository.findAll();

        assertEquals(beforePosts.size() + 1, afterPosts.size());
        assertNotEquals(beforeHashTagList.size(), afterHashTagList.size());
        assertNotEquals(beforeImageList.size(), afterImageList.size());
    }

    @DisplayName("포스트 조회 - 처음 조회시")
    @Test
    void findAllLastIdZero() throws Exception {
        Post post1 = Post.builder().content("post1").build();
        Post post2 = Post.builder().content("post2").build();
        Post post3 = Post.builder().content("post3").build();
        Post post4 = Post.builder().content("post4").build();

        postService.write(post1,user.getId(),new ArrayList<>());
        postService.write(post2,user.getId(),new ArrayList<>());
        postService.write(post3,user.getId(),new ArrayList<>());
        Post writedPost = postService.write(post4, user.getId(), new ArrayList<>());

        postService.like(writedPost.getId(),user.getId(),user.getId());

        mockMvc.perform(get("/api/user/" + user.getId() +"/post/list?lastId=0&size=2")
                .header(tokenHeader,apiToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.size()").value(2))
                .andExpect(jsonPath("$.response.[0].likesOfMe").value(true))
                .andDo(print());


    }

    @DisplayName("포스트 조회 - 인피니트 스크롤링 이후")
    @Test
    void findAllLastIdNotZero() throws Exception {
        Post post1 = Post.builder().content("post1").build();
        Post post2 = Post.builder().content("post2").build();
        Post post3 = Post.builder().content("post3").build();
        Post post4 = Post.builder().content("post4").build();

        Post writedPost = postService.write(post1, user.getId(), new ArrayList<>());
        Post lastPost = postService.write(post2, user.getId(), new ArrayList<>());
        postService.write(post3,user.getId(),new ArrayList<>());
        postService.write(post4, user.getId(), new ArrayList<>());

        postService.like(writedPost.getId(),user.getId(),user.getId());

        mockMvc.perform(get("/api/user/" + user.getId() +"/post/list?lastId=" + lastPost.getId() +"&size=2")
                .header(tokenHeader,apiToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.size()").value(1))
                .andExpect(jsonPath("$.response.[0].likesOfMe").value(true))
                .andDo(print());
    }

    @DisplayName("해시태그 검색")
    @Test
    void postsOfHashTag() throws Exception {
        Post post1 = Post.builder().content("#hashtag").build();
        Post post2 = Post.builder().content("#hashtag").build();
        Post post3 = Post.builder().content("#hashtag").build();
        Post post4 = Post.builder().content("#hashtag").build();

        postService.write(post1,user.getId(),new ArrayList<>());
        postService.write(post2,user.getId(),new ArrayList<>());
        postService.write(post3,user.getId(),new ArrayList<>());
        postService.write(post4,user.getId(),new ArrayList<>());

        mockMvc.perform(get("/api/post/hashtag/list?lastId=3&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.size()").value(2))
                .andExpect(jsonPath("$.response.[0].id").value(2))
                .andDo(print());
    }

    @DisplayName("포스트 삭제")
    @Test
    void removePost() throws Exception {
        Post post1 = Post.builder().content("post1").build();
        Post savedPost = postService.write(post1, user.getId(), new ArrayList<>());

        mockMvc.perform(delete("/api/post/" + savedPost.getId())
                .header(tokenHeader,apiToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value(savedPost.getId()))
                .andDo(print());

        assertNull(postService.findById(savedPost.getId()).orElse(null));
    }

    @DisplayName("좋아요")
    @Test
    void like() throws Exception {
        User user2 = userService.join("test2",new Email("test2@gmail.com"),"12345678");

        Post post1 = Post.builder().content("post1").build();
        Post savedPost = postService.write(post1, user2.getId(), new ArrayList<>());

        mockMvc.perform(patch("/api/user/" + user2.getId() + "/post/" + savedPost.getId() +"/like")
                .header(tokenHeader,apiToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.likesOfMe").value(true))
                .andDo(print());

    }

    @DisplayName("좋아요 취소")
    @Test
    void unlike() throws Exception {
        User user2 = userService.join("test2",new Email("test2@gmail.com"),"12345678");

        Post post1 = Post.builder().content("post1").build();
        Post savedPost = postService.write(post1, user2.getId(), new ArrayList<>());
        postService.like(savedPost.getId(),user.getId(),user2.getId());

        mockMvc.perform(delete("/api/user/" + user2.getId() + "/post/" + savedPost.getId() +"/unlike")
                .header(tokenHeader,apiToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.likesOfMe").value(false))
                .andDo(print());

    }

    @DisplayName("댓글 작성")
    @Test
    void comment() throws Exception {
        User user2 = userService.join("test2",new Email("test2@gmail.com"),"12345678");

        Post post1 = Post.builder().content("post1").build();
        Post savedPost = postService.write(post1, user2.getId(), new ArrayList<>());
        CommentRequest commentRequest = new CommentRequest("comment1");

        mockMvc.perform(post("/api/user/" + user2.getId() + "/post/" + savedPost.getId() +"/comment")
                .header(tokenHeader,apiToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.content").value(commentRequest.getContent()))
                .andDo(print());

        List<Comment> comments = commentService.findAll(savedPost.getId(),user.getId(),null);
        assertNotNull(comments);
        assertEquals(comments.size(),1);

    }

    @DisplayName("리트윗")
    @Test
    void retweet() throws Exception {
        User user2 = userService.join("test2",new Email("test2@gmail.com"),"12345678");

        Post post1 = Post.builder().content("post1").build();
        Post savedPost = postService.write(post1, user2.getId(), new ArrayList<>());

        mockMvc.perform(post("/api/post/" + savedPost.getId() +"/retweet")
                .header(tokenHeader,apiToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.isRetweet").value(true))
                .andDo(print());
    }
}
