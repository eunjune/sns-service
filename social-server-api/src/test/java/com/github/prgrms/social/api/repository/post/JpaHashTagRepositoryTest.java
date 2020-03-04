package com.github.prgrms.social.api.repository.post;

import com.github.prgrms.social.api.error.NotFoundException;
import com.github.prgrms.social.api.model.post.Comment;
import com.github.prgrms.social.api.model.post.HashTag;
import com.github.prgrms.social.api.model.post.Post;
import com.github.prgrms.social.api.model.user.ConnectedUser;
import com.github.prgrms.social.api.model.user.Email;
import com.github.prgrms.social.api.model.user.Likes;
import com.github.prgrms.social.api.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class JpaHashTagRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    JpaHashTagRepository hashTagRepository;

    @Autowired
    JpaPostRepository postRepository;

    @BeforeEach
    void beforeEach() {
        HashTag hashTag1 = HashTag.builder().name("first").build();
        HashTag hashTag2 = HashTag.builder().name("second").build();

        Likes like = new Likes(null,null);

        Comment comment1 = Comment.builder()
                .content("first comment")
                .build();

        Comment comment2 = Comment.builder()
                .content("first comment")
                .build();

        Post post1 = Post.builder()
                .content("test01 first post #first")
                .build();
        post1.incrementAndGetComments(comment1);
        post1.incrementAndGetLikes(like);
        post1.addHashTag(hashTag1);

        Post post2 = Post.builder()
                .content("test01 second post #second")
                .build();

        post2.addHashTag(hashTag2);

        Post post3 = Post.builder()
                .content("test01 third post")
                .build();

        Post post4 = Post.builder()
                .content("test02 third post #first")
                .build();
        post4.incrementAndGetComments(comment2);
        post4.addHashTag(hashTag1);

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
    void findByName() {
        HashTag hashTag = hashTagRepository.findByName("first").orElseThrow(() -> new NotFoundException(HashTag.class, "first"));

        assertEquals(hashTag.getPosts().size(),2);

        HashTag hashTag2 = hashTagRepository.findByName("second").orElseThrow(() -> new NotFoundException(HashTag.class, "second"));

        assertEquals(hashTag2.getPosts().size(),1);

    }
}
