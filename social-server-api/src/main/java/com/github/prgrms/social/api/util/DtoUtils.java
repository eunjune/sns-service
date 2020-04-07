package com.github.prgrms.social.api.util;

import com.github.prgrms.social.api.model.api.response.post.CommentResponse;
import com.github.prgrms.social.api.model.api.response.post.PostResponse;
import com.github.prgrms.social.api.model.api.response.user.AuthenticationResponse;
import com.github.prgrms.social.api.model.api.response.user.MeResponse;
import com.github.prgrms.social.api.model.api.response.user.UserResponse;
import com.github.prgrms.social.api.model.post.Comment;
import com.github.prgrms.social.api.model.post.Post;
import com.github.prgrms.social.api.model.user.User;
import com.github.prgrms.social.api.security.AuthenticationResult;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

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

    public CommentResponse convertCommentResponse(Comment comment) {
        CommentResponse commentResponse = new CommentResponse();
        modelMapper.map(comment,commentResponse);

        commentResponse.setUser(this.convertUserResponse(comment.getUser()));
        return commentResponse;
    }
}
