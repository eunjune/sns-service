package com.github.prgrms.social.api.event;

import com.github.prgrms.social.api.model.post.Comment;
import com.github.prgrms.social.api.model.post.Post;
import com.github.prgrms.social.api.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static com.google.common.base.Preconditions.checkNotNull;

@NoArgsConstructor(force = true)
@Getter
@ToString
@AllArgsConstructor
public class CommentEvent {

    private User me;

    private Post targetPost;

    private String comment;
}
