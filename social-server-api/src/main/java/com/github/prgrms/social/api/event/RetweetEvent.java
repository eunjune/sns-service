package com.github.prgrms.social.api.event;

import com.github.prgrms.social.api.model.post.Post;
import com.github.prgrms.social.api.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(force = true)
@Getter
@ToString
@AllArgsConstructor
public class RetweetEvent {

    private User me;

    private Post targetPost;
}
