/*
package com.github.prgrms.social.api.repository.jdbc.post;

import com.github.prgrms.social.api.configure.support.Pageable;
import com.github.prgrms.social.api.model.commons.Id;
import com.github.prgrms.social.api.model.post.Post;
import com.github.prgrms.social.api.model.post.Writer;
import com.github.prgrms.social.api.model.user.Email;
import com.github.prgrms.social.api.model.user.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import static com.github.prgrms.social.api.util.DateTimeUtils.dateTimeOf;
import static com.github.prgrms.social.api.util.DateTimeUtils.timestampOf;
import static java.util.Optional.ofNullable;

@Repository
public class JdbcPostRepository implements PostRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcPostRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Post save(Post post) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO posts(seq,user_seq,contents,like_count,comment_count,create_at) VALUES (null,?,?,?,?,?)", new String[]{"seq"});
           // ps.setLong(1, post.getUserId().getValue());
            ps.setString(2, post.getContents());
            //ps.setInt(3, post.getLikes());
            //ps.setInt(4, post.getComments());
            ps.setTimestamp(5, timestampOf(post.getCreateAt()));
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        long generatedSeq = key != null ? key.longValue() : -1;
        return Post.builder()
                .seq(generatedSeq)
                //.userId(post.getUserId())
                .contents(post.getContents())
                //.likes(post.getLikes())
                //.comments(post.getComments())
                .createAt(post.getCreateAt())
                .build();
    }

    @Override
    public void update(Post post) {
        jdbcTemplate.update("UPDATE posts SET contents=?,like_count=?,comment_count=? WHERE seq=?",
                post.getContents(),
                //post.getLikes(),
                //post.getComments(),
                post.getSeq()
        );
    }

    @Override
    public Optional<Post> findById(Id<Post, Long> postId, Id<User, Long> postWriterId, Id<User, Long> userId) {
        List<Post> results = jdbcTemplate.query(
                "SELECT p.*,u.email,u.name, NVL2(l.user_seq,true,false) likes_of_me " +
                        "FROM posts p JOIN users u ON p.user_seq=u.seq LEFT JOIN likes l ON l.user_seq=? AND l.post_seq = p.seq " +
                        "WHERE p.user_seq=? AND p.seq=?",
                new Object[]{userId.getValue(), postWriterId.getValue(), postId.getValue()},
                mapper
        );
        return ofNullable(results.isEmpty() ? null : results.get(0));
    }

    @Override
    public List<Post> findAll(Id<User, Long> userId, Id<User, Long> postWriterId, Pageable pageable) {
        return jdbcTemplate.query(
                "SELECT p.*,u.email,u.name, NVL2(l.user_seq,true,false) likes_of_me " +
                        "FROM posts p JOIN users u ON p.user_seq=u.seq LEFT JOIN likes l ON l.user_seq=? AND l.post_seq = p.seq " +
                        "WHERE p.user_seq=? ORDER BY p.seq DESC LIMIT ? OFFSET ?",
                new Object[]{userId.getValue(), postWriterId.getValue(), pageable.limit(), pageable.offset()},
                mapper
        );
    }

    static RowMapper<Post> mapper = (rs, rowNum) -> Post.builder()
            .seq(rs.getLong("seq"))
            //.userId(Id.of(User.class, rs.getLong("user_seq")))
            .contents(rs.getString("contents"))
            //.likes(rs.getInt("like_count"))
            .likesOfMe(rs.getBoolean("likes_of_me"))
            //.comments(rs.getInt("comment_count"))
            .writer(new Writer(new Email(rs.getString("email")), rs.getString("name")))
            .createAt(dateTimeOf(rs.getTimestamp("create_at")))
            .build();

}
*/
