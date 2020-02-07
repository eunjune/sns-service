/*
package com.github.prgrms.social.api.repository.jdbc.post;

import com.github.prgrms.social.api.model.commons.Id;
import com.github.prgrms.social.api.model.post.Post;
import com.github.prgrms.social.api.model.user.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcPostLikeRepository implements PostLikeRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcPostLikeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void like(Id<User, Long> userId, Id<Post, Long> postId) {
        jdbcTemplate.update("INSERT INTO likes(user_seq,post_seq) VALUES(?,?)", userId.getValue(), postId.getValue());
    }

}
*/
