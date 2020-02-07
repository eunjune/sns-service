package com.github.prgrms.social.api.repository.post;

import com.github.prgrms.social.api.error.NotFoundException;
import com.github.prgrms.social.api.model.post.Comment;
import com.github.prgrms.social.api.model.post.Post;
import com.github.prgrms.social.api.model.user.ConnectedUser;
import com.github.prgrms.social.api.model.user.Email;
import com.github.prgrms.social.api.model.user.Likes;
import com.github.prgrms.social.api.model.user.User;
import com.github.prgrms.social.api.repository.jpa.post.JpaCommentRepository;
import com.github.prgrms.social.api.repository.jpa.post.JpaPostRepository;
import com.github.prgrms.social.api.repository.jpa.user.JpaUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class JpaCommentRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    JpaCommentRepository jpaCommentRepository;

    @Autowired
    JpaPostRepository jpaPostRepository;

    @Autowired
    JpaUserRepository jpaUserRepository;

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
        Comment comment = Comment.builder()
                .contents("second comment")
                .build();

        User user = jpaUserRepository.findBySeq(1L).orElseThrow(()->new NotFoundException(User.class, 1L));

        Post post = jpaPostRepository.findBySeq(1L).orElseThrow(()->new NotFoundException(Post.class, 1L));

        user.addComment(comment);
        post.incrementAndGetComments(comment);

        Comment savedComment = jpaCommentRepository.save(comment);

        assertEquals(savedComment.getSeq(),3L);
        assertEquals(savedComment.getUser().getSeq(),1L);
        assertEquals(savedComment.getPost().getSeq(),1L);
    }

    @Test
    void findBySeq() {
        Comment comment = jpaCommentRepository.findBySeq(1L)
                .orElseThrow(() -> new NotFoundException(Comment.class, 1L));

        assertEquals(comment.getSeq(), 1L);
    }

    @Test
    void findByPost_SeqOrderBySeqDesc() {
        List<Comment> comments = jpaPostRepository.findBySeq(1L)
                .map(post -> jpaCommentRepository.findByPost_SeqOrderBySeqDesc(post.getSeq()))
                .orElseThrow(() -> new NotFoundException(Post.class, 1L));

        assertEquals(comments.size(),1);
    }
}