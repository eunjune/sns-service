package com.github.prgrms.social.api.repository.user;

import com.github.prgrms.social.api.error.NotFoundException;
import com.github.prgrms.social.api.model.post.Comment;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;


@DataJpaTest
public class JpaUserRepositoryTests {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    JpaUserRepository jpaUserRepository;

    /*
    * INSERT INTO users(seq,name,email,passwd) VALUES (null,'tester00','test00@gmail.com','$2a$10$mzF7/rMylsnxxwNcTsJTEOFhh1iaHv3xVox.vpf6JQybEhE4jDZI.');
INSERT INTO users(seq,name,email,passwd) VALUES (null,'tester01','test01@gmail.com','$2a$10$Mu/akK4gI.2RHm7BQo/kAO1cng2TUgxpoP.zBbPOeccVGP4lKVGYy');
INSERT INTO users(seq,name,email,passwd) VALUES (null,'tester02','test02@gmail.com','$2a$10$hO38hmoHN1k7Zm3vm95C2eZEtSOaiI/6xZrRAx8l0e78i9.NK8bHG');

INSERT INTO posts(seq,user_seq,contents,like_count,comment_count,create_at) VALUES (null,1,'test01 first post',1,1,'2019-03-01 13:10:00');
INSERT INTO posts(seq,user_seq,contents,like_count,comment_count,create_at) VALUES (null,1,'test01 second post',0,0,'2019-03-12 09:45:00');
INSERT INTO posts(seq,user_seq,contents,like_count,comment_count,create_at) VALUES (null,1,'test01 third post',0,0,'2019-03-20 19:05:00');
INSERT INTO posts(seq,user_seq,contents,like_count,comment_count,create_at) VALUES (null,2,'test02 post',0,1,'2019-03-20 15:13:20');

INSERT INTO comments(seq,user_seq,post_seq,contents,create_at) VALUES (null,1,1,'first comment','2019-03-01 13:15:00');
INSERT INTO comments(seq,user_seq,post_seq,contents,create_at) VALUES (null,2,4,'first comment','2019-03-01 13:15:00');

INSERT INTO connections(seq,user_seq,target_seq,granted_at,create_at) VALUES (null,1,2,'2019-03-31 13:00:00','2019-03-31 00:10:00');

INSERT INTO likes(seq,user_seq,post_seq,create_at) VALUES (null,1,1,'2019-03-01 15:10:00');*/

    @BeforeEach
    void beforeEach() {
        Likes like = new Likes(null,null);

        Comment comment1 = Comment.builder()
                .contents("first comment")
                .build();

        Comment comment2 = Comment.builder()
                .contents("first comment")
                .build();

        Post post1 = Post.builder()
                .contents("test01 first post")
                .build();
        post1.incrementAndGetComments(comment1);
        post1.incrementAndGetLikes(like);

        Post post2 = Post.builder()
                .contents("test01 second post")
                .build();

        Post post3 = Post.builder()
                .contents("test01 third post")
                .build();

        Post post4 = Post.builder()
                .contents("test02 third post")
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
        User user = User.builder()
                .name("jpatest")
                .email(new Email("jpatest@gmail.com"))
                .password("$2a$10$hO38hmoHN1k7Zm3vm95C2eZEtSOaiI/6xZrRAx8l0e78i9.NK8bHG")
                .build();

        User savedUser = jpaUserRepository.save(user);

        System.out.println(savedUser);

        assertNotNull(savedUser.getSeq());
    }

    @Test
    void findBySeq() {
        User user1 = jpaUserRepository.findBySeq(1L).orElseThrow(()->new NotFoundException(User.class,1L));
        User user2 = jpaUserRepository.findBySeq(2L).orElseThrow(()->new NotFoundException(User.class,2L));
        User user3 = jpaUserRepository.findBySeq(3L).orElseThrow(()->new NotFoundException(User.class,3L));

        assertEquals(user1.getSeq(),1L);
        assertEquals(user2.getSeq(),2L);
        assertEquals(user3.getSeq(),3L);
    }

    @Test
    void findByEmail() {
        User user1 = jpaUserRepository.findByEmail(new Email("test01@gmail.com")).orElseThrow(()->new NotFoundException(User.class,1L));
        User user2 = jpaUserRepository.findByEmail(new Email("test02@gmail.com")).orElseThrow(()->new NotFoundException(User.class,2L));
        User user3 = jpaUserRepository.findByEmail(new Email("test03@gmail.com")).orElseThrow(()->new NotFoundException(User.class,3L));

        assertEquals(user1.getSeq(),1L);
        assertEquals(user2.getSeq(),2L);
        assertEquals(user3.getSeq(),3L);
    }

}
