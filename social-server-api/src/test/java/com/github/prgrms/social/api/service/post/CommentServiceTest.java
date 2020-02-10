
package com.github.prgrms.social.api.service.post;

import com.github.prgrms.social.api.model.post.Comment;
import com.github.prgrms.social.api.model.post.Post;
import com.github.prgrms.social.api.model.user.Email;
import com.github.prgrms.social.api.model.user.User;
import com.github.prgrms.social.api.repository.post.JpaCommentRepository;
import com.github.prgrms.social.api.repository.post.JpaPostRepository;
import com.google.common.eventbus.EventBus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private JpaPostRepository postRepository;

    @Mock
    private JpaCommentRepository commentRepository;

    @Mock
    private EventBus eventBus;


    @DisplayName("코멘트를 작성한다")
    @Test
    void write() {
        Post givenPost = Post.builder().seq(1L).contents(randomAlphabetic(40)).build();
        User user1 = User.builder().seq(1L).name("test1").email(new Email("test1@gmail.com")).password("1234").build();
        user1.addPost(givenPost);

        User givenUser = User.builder().seq(1L).name("test1").email(new Email("test1@gmail.com")).password("1234").build();
        Comment givenComment = Comment.builder().seq(1L).contents(randomAlphabetic(20)).build();
        givenUser.addComment(givenComment);

        given(postRepository.findById(1L,2L,1L)).willReturn(Optional.ofNullable(givenPost));
        given(commentRepository.save(Comment.builder().contents(randomAlphabetic(20)).build())).willReturn(givenComment);

        commentService.write(1L, 2L, 1L, Comment.builder().contents(randomAlphabetic(20)).build());

        then(postRepository).should(times(1)).findById(any(),any(),any());
        then(postRepository).should(times(1)).save(any());
        then(commentRepository).should(times(1)).save(any());
        then(eventBus).should(times(1)).post(any());

        assertEquals(givenPost.getCommentList().size(),1);
        assertEquals(user1.getCommentList().size(),1);
    }


    @DisplayName("댓글 목록을 조회한다")
    @Test
    void findAll () {
        Post post1 = Post.builder().seq(1L).contents(randomAlphabetic(40)).build();
        User user1 = User.builder().seq(1L).name("test1").email(new Email("test1@gmail.com")).password("1234").build();
        user1.addPost(post1);
        Comment comment = Comment.builder().seq(1L).contents(randomAlphabetic(20)).build();
        user1.addComment(comment);

        List<Comment> givenComments = new ArrayList<>();
        givenComments.add(comment);

        given(postRepository.findById(1L,2L,1L)).willReturn(Optional.ofNullable(post1));
        given(commentRepository.findByPost_SeqOrderBySeqDesc(1L)).willReturn(givenComments);

        commentService.findAll(1L,2L,1L);

        then(postRepository).should(times(1)).findById(any(),any(),any());
        then(commentRepository).should(times(1)).findByPost_SeqOrderBySeqDesc(any());
    }

}
