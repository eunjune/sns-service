package com.github.prgrms.social.api.util;

import com.github.prgrms.social.api.model.api.response.post.CommentResponse;
import com.github.prgrms.social.api.model.api.response.post.MyPostResponse;
import com.github.prgrms.social.api.model.api.response.post.PostResponse;
import com.github.prgrms.social.api.model.api.response.user.AuthenticationResponse;
import com.github.prgrms.social.api.model.api.response.user.MeResponse;
import com.github.prgrms.social.api.model.api.response.user.NotificationResponse;
import com.github.prgrms.social.api.model.api.response.user.UserResponse;
import com.github.prgrms.social.api.model.post.Comment;
import com.github.prgrms.social.api.model.post.Post;
import com.github.prgrms.social.api.model.user.Notification;
import com.github.prgrms.social.api.model.user.User;
import com.github.prgrms.social.api.security.AuthenticationResult;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DtoUtils {

    private final ModelMapper modelMapper;

    public UserResponse convertUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        modelMapper.map(user, userResponse);

        return userResponse;
    }

    public MeResponse convertMeResponse(User user) {
        MeResponse meResponse = new MeResponse();
        modelMapper.map(user, meResponse);

        return meResponse;
    }

    public AuthenticationResponse convertAuthenticationResponse(AuthenticationResult authenticationResult) {
        return new AuthenticationResponse(authenticationResult.getToken(), convertMeResponse(authenticationResult.getUser()));
    }

    public PostResponse convertPostResponse(Post post) {
        PostResponse postResponse = new PostResponse();
        modelMapper.map(post,postResponse);

        postResponse.setUser(this.convertUserResponse(post.getUser()));

        if(post.getRetweetPost() != null) {
            PostResponse retweetResponse = new PostResponse();
            modelMapper.map(post.getRetweetPost(), retweetResponse);

            retweetResponse.setUser(this.convertUserResponse(post.getRetweetPost().getUser()));
            postResponse.setRetweetPost(retweetResponse);
        }

        return postResponse;
    }

    public MyPostResponse convertMyPostResponse(Post post) {
        MyPostResponse myPostResponse = new MyPostResponse();
        modelMapper.map(post,myPostResponse);

        myPostResponse.setUser(this.convertUserResponse(post.getUser()));

        Set<CommentResponse> commentResponseSet = new HashSet<>();
        for(Comment comment : post.getComments()) {
            commentResponseSet.add(this.convertCommentResponse(comment));
        }

        myPostResponse.setComments(commentResponseSet);

        return myPostResponse;
    }

    public CommentResponse convertCommentResponse(Comment comment) {
        CommentResponse commentResponse = new CommentResponse();
        modelMapper.map(comment,commentResponse);

        commentResponse.setUser(this.convertUserResponse(comment.getUser()));
        return commentResponse;
    }

    public NotificationResponse convertNotificationResponse(Notification notification) {
        NotificationResponse notificationResponse = new NotificationResponse();
        modelMapper.map(notification, notificationResponse);
        return notificationResponse;
    }
}
