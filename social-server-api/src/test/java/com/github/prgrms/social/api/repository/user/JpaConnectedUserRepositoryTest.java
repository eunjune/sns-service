package com.github.prgrms.social.api.repository.user;

import com.github.prgrms.social.api.model.post.Comment;
import com.github.prgrms.social.api.model.post.Post;
import com.github.prgrms.social.api.model.user.ConnectedUser;
import com.github.prgrms.social.api.model.user.Email;
import com.github.prgrms.social.api.model.user.Likes;
import com.github.prgrms.social.api.model.user.User;
import com.github.prgrms.social.api.repository.post.projection.ConnectedId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class JpaConnectedUserRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    JpaConnectedUserRepository jpaConnectedUserRepository;

    @Autowired
    JpaUserRepository jpaUserRepository;

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

        User user4 = User.builder()
                .name("test04")
                .email(new Email("test04]@gmail.com"))
                .password("$2a$10$hO38hmoHN1k7Zm3vm95C2eZEtSOaiI/6xZrRAx8l0e78i9.NK8bHG")
                .build();

        User user5 = User.builder()
                .name("test05")
                .email(new Email("test05@gmail.com"))
                .password("$2a$10$hO38hmoHN1k7Zm3vm95C2eZEtSOaiI/6xZrRAx8l0e78i9.NK8bHG")
                .build();

        ConnectedUser connectedUser = new ConnectedUser(null,null);
        connectedUser.setTargetUser(user2);
        user1.addConnectedUser(connectedUser);

        ConnectedUser connectedUser2 = new ConnectedUser(null,null);
        connectedUser2.setTargetUser(user4);
        user1.addConnectedUser(connectedUser2);

        ConnectedUser connectedUser3 = new ConnectedUser(null,null);
        connectedUser3.setTargetUser(user5);
        user1.addConnectedUser(connectedUser3);

        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.persist(user3);
        entityManager.persist(user4);
        entityManager.persist(user5);
        entityManager.flush();
    }

    @Test
    void findAllConnectedUser() {
        List<ConnectedUser> connects = jpaConnectedUserRepository.findByUser_IdAndCreateAtIsNotNullOrderByIdDesc(1L);

        assertEquals(connects.size(),3);
    }

    @Test
    void findConnectedId() {
        List<ConnectedId> connectedIds = jpaConnectedUserRepository.findByUser_IdAndCreateAtIsNotNullOrderByTargetUser_Id(1L);

        assertEquals(connectedIds.size(),3);
    }

}
