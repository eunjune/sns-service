package com.github.prgrms.social.api.repository.post;

import com.github.prgrms.social.api.error.NotFoundException;
import com.github.prgrms.social.api.model.post.Comment;
import com.github.prgrms.social.api.model.post.HashTag;
import com.github.prgrms.social.api.model.post.Post;
import com.github.prgrms.social.api.model.user.ConnectedUser;
import com.github.prgrms.social.api.model.user.Email;
import com.github.prgrms.social.api.model.user.Likes;
import com.github.prgrms.social.api.model.user.User;
import com.github.prgrms.social.api.repository.user.JpaUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class JpaPostRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    JpaPostRepository jpaPostRepository;

    @Autowired
    JpaUserRepository jpaUserRepository;

    @Autowired
    JpaHashTagRepository jpaHashTagRepository;

    @BeforeEach
    void beforeEach() {
        Likes like = new Likes(null,null);

        Comment comment1 = Comment.builder()
                .content("first comment")
                .build();

        Comment comment2 = Comment.builder()
                .content("first comment")
                .build();

        Post post1 = Post.builder()
                .content("test01 first post")
                .build();
        post1.incrementAndGetComments(comment1);
        post1.incrementAndGetLikes(like);

        Post post2 = Post.builder()
                .content("test01 second post")
                .build();

        Post post3 = Post.builder()
                .content("test01 third post")
                .build();

        Post post4 = Post.builder()
                .content("test02 third post")
                .build();
        post4.incrementAndGetComments(comment2);

        User user1 = User.builder()
                .name("tester01")
                .email(new Email("test01@gmail.com"))
                .password("$2a$10$mzF7/rMylsnxxwNcTsJTEOFhh1iaHv3xVox.vpf6JQybEhE4jDZI.")
                .build();
        user1.addPost(post1);
        user1.addPost(post2);
        user1.addPost(post3);
        user1.addComment(comment1);
        user1.addLike(like);

        User user2 = User.builder()
                .name("tester02")
                .email(new Email("test02@gmail.com"))
                .password("$2a$10$Mu/akK4gI.2RHm7BQo/kAO1cng2TUgxpoP.zBbPOeccVGP4lKVGYy")
                .build();
        user2.addPost(post4);

        User user3 = User.builder()
                .name("tester03")
                .email(new Email("test03@gmail.com"))
                .password("$2a$10$hO38hmoHN1k7Zm3vm95C2eZEtSOaiI/6xZrRAx8l0e78i9.NK8bHG")
                .build();

        ConnectedUser connectedUser = new ConnectedUser(null,null);
        connectedUser.setTargetUser(user2);

        user1.addConnectedUser(connectedUser);

        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.persist(user3);
        entityManager.flush();
    }

    @Test
    void save() {
        Post savedPost = jpaUserRepository.findById(1L)
                .map(user -> {
                    Post post = Post.builder()
                            .content("test01 fourth post #hashtag1 #()** #Hashtag2 #hash_tag")
                            .build();

                    List<HashTag> hashTags = post.findHashTag();
                    for(HashTag hashTag : hashTags) {
                        post.addHashTag(jpaHashTagRepository.save(hashTag));
                    }
                    user.addPost(post);

                    return jpaPostRepository.save(post);
                })
                .orElseThrow(() -> new NotFoundException(User.class, 1L));

        assertEquals(savedPost.getId(), 5L);
        assertEquals(savedPost.getUser().getId(), 1L);
        assertEquals(savedPost.getHashTags().size(), 3);
    }

    @Test
    void findById() {
        Post post = jpaPostRepository.findByIdCustom(1L,1L,1L).orElseThrow(()->new NotFoundException(Post.class,1L));

        assertEquals(post.getId(),1L);
        assertEquals(post.getUser().getId(),1L);
        assertTrue(post.isLikesOfMe());

        Post post2 = jpaPostRepository.findByIdCustom(1L,2L,1L).orElseThrow(()->new NotFoundException(Post.class,1L));

        assertEquals(post2.getId(),1L);
        assertEquals(post2.getUser().getId(),1L);
        assertFalse(post2.isLikesOfMe());
    }

    @Test
    void findAll() {
        int size = 4;
        Pageable pageable = PageRequest.of(0,size);

        List<Post> posts = jpaPostRepository.findAll(1L,1L,pageable);

        assertEquals(posts.size(),size);
    }
}
