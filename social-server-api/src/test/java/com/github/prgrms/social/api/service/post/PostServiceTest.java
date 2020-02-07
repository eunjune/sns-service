

package com.github.prgrms.social.api.service.post;

import com.github.prgrms.social.api.model.post.Post;
import com.github.prgrms.social.api.model.user.Email;
import com.github.prgrms.social.api.model.user.Likes;
import com.github.prgrms.social.api.model.user.User;
import com.github.prgrms.social.api.repository.jpa.post.JpaPostLikeRepository;
import com.github.prgrms.social.api.repository.jpa.post.JpaPostRepository;
import com.github.prgrms.social.api.repository.jpa.user.JpaUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private JpaUserRepository userRepository;

    @Mock
    private JpaPostRepository postRepository;

    @Mock
    private JpaPostLikeRepository postLikeRepository;


    @DisplayName("포스트를 작성한다")
    @Test
    void write() {
        User user = User.builder().seq(1L).name("test").password("1234").email(new Email("test00@gmail.com")).build();
        Post post = Post.builder().contents(randomAlphabetic(40)).build();

        Post givenPost = Post.builder().seq(1L).contents(randomAlphabetic(40)).build();

        given(userRepository.findBySeq(1L)).willReturn(Optional.ofNullable(user));
        given(postRepository.save(post)).willReturn(givenPost);

        postService.write(post, 1L);

        then(userRepository).should(times(1)).findBySeq(any());
        then(postRepository).should(times(1)).save(any());

        assertNotNull(post.getUser());
    }


    @DisplayName("포스트를 조회한다")
    @Test
    void findAll() {

        int size = 4;
        int page = 0;
        Pageable pageable = PageRequest.of(page,size);

        List<Post> givenPosts = new ArrayList<>();
        Post post1 = Post.builder().seq(1L).contents(randomAlphabetic(40)).build();
        Post post2 = Post.builder().seq(2L).contents(randomAlphabetic(40)).build();
        Post post3 = Post.builder().seq(3L).contents(randomAlphabetic(40)).build();
        Post post4 = Post.builder().seq(4L).contents(randomAlphabetic(40)).build();

        givenPosts.add(post1);
        givenPosts.add(post2);
        givenPosts.add(post3);
        givenPosts.add(post4);

        given(userRepository.findBySeq(1L)).willReturn(Optional.ofNullable(User.builder().seq(1L).name("test").password("1234").email(new Email("test00@gmail.com")).build()));
        given(postRepository.findAll(2L, 1L, pageable)).willReturn(givenPosts.subList(page,page + size));

        List<Post> returnPosts = postService.findAll(2L, 1L, pageable);

        then(userRepository).should(times(1)).findBySeq(any());
        then(postRepository).should(times(1)).findAll(any(), any(), any());

        assertNotNull(returnPosts);
        assertEquals(returnPosts.size(), size);

    }


    @DisplayName("포스트를 처음으로 좋아한다")
    @Test
    void like_first() {

        User user1 = User.builder().seq(1L).name("test1").email(new Email("test1@gmail.com")).password("1234").build();
        User user3 = User.builder().seq(3L).name("test3").email(new Email("test3@gmail.com")).password("1234").build();
        User user4 = User.builder().seq(4L).name("test4").email(new Email("test4@gmail.com")).password("1234").build();

        Likes like1 = new Likes(null,null);
        Likes like2 = new Likes(null,null);

        user3.addLike(like1);
        user4.addLike(like2);

        Post givenPost = Post.builder().seq(1L).contents(randomAlphabetic(40)).build();
        user1.addPost(givenPost);
        givenPost.incrementAndGetLikes(like2);
        givenPost.incrementAndGetLikes(like1);
        givenPost.setLikesOfMe(1L);
        int givenLikeSize = givenPost.getLikeList().size();

        given(postRepository.findById(1L,1L,1L)).willReturn(Optional.of(givenPost));

        Post returnPost = postService.like(1L, 1L, 1L).orElse(null);

        then(postRepository).should(times(1)).findById(any(),any(),any());
        then(postLikeRepository).should(times(1)).save(any());

        assertEquals(returnPost.getLikeList().size(), givenLikeSize + 1);

    }


    @DisplayName("포스트를 중복으로 좋아할수없다")
    @Test
    void like_overlap() {

        User user3 = User.builder().seq(3L).name("test3").email(new Email("test3@gmail.com")).password("1234").build();
        User user4 = User.builder().seq(4L).name("test4").email(new Email("test4@gmail.com")).password("1234").build();

        Likes like1 = new Likes(null,null);
        Likes like2 = new Likes(null,null);

        user3.addLike(like1);
        user4.addLike(like2);

        Post givenPost = Post.builder().seq(1L).contents(randomAlphabetic(40)).build();
        user3.addPost(givenPost);
        givenPost.incrementAndGetLikes(like2);
        givenPost.incrementAndGetLikes(like1);
        givenPost.setLikesOfMe(3L);
        int givenLikeSize = givenPost.getLikeList().size();

        given(postRepository.findById(1L,3L,1L)).willReturn(Optional.of(givenPost));

        Post returnPost = postService.like(1L, 3L, 1L).orElse(null);

        then(postRepository).should(times(1)).findById(any(),any(),any());
        then(postLikeRepository).shouldHaveNoMoreInteractions();

        assertEquals(returnPost.getLikeList().size(), givenLikeSize);
    }

}
