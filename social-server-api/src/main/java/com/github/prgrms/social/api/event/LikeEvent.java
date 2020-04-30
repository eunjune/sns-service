package com.github.prgrms.social.api.event;

import com.github.prgrms.social.api.model.post.Post;
import com.github.prgrms.social.api.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LikeEvent {

    private User me;

    private Post targetPost;
}
