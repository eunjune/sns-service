package com.github.prgrms.social.api.repository.post;

import com.github.prgrms.social.api.configure.support.Pageable;
import com.github.prgrms.social.api.model.commons.Id;
import com.github.prgrms.social.api.model.post.Post;
import com.github.prgrms.social.api.model.user.User;

import java.util.List;
import java.util.Optional;

public interface PostRepository {

    Post save(Post post);

    void update(Post post);

    Optional<Post> findById(Id<Post, Long> postId, Id<User, Long> postWriterId, Id<User, Long> userId);

    List<Post> findAll(Id<User, Long> userId, Id<User, Long> postWriterId, Pageable pageable);

}