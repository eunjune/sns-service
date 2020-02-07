package com.github.prgrms.social.api.repository.jdbc.post;

import com.github.prgrms.social.api.model.commons.Id;
import com.github.prgrms.social.api.model.post.Post;
import com.github.prgrms.social.api.model.user.User;

public interface PostLikeRepository {

    void like(Id<User, Long> userId, Id<Post, Long> postId);

}
