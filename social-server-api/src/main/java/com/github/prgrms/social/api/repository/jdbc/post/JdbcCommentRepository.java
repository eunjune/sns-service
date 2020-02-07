/*
package com.github.prgrms.social.api.repository.jdbc.post;

import com.github.prgrms.social.api.model.commons.Id;
import com.github.prgrms.social.api.model.post.Comment;
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
public class JdbcCommentRepository implements CommentRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcCommentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Comment save(Comment comment) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO comments(seq,user_seq,post_seq,contents,create_at) VALUES (null,?,?,?,?)", new String[]{"seq"});
            //ps.setLong(1, comment.getUserId().getValue());
            //ps.setLong(2, comment.getPostId().getValue());
            ps.setString(3, comment.getContents());
            ps.setTimestamp(4, timestampOf(comment.getCreateAt()));
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        long generatedSeq = key != null ? key.longValue() : -1;

        return Comment.builder()
                .seq(generatedSeq)
                //.userId(comment.getUserId())
                //.postId(comment.getPostId())
                .contents(comment.getContents())
                .createAt(comment.getCreateAt())
                .build();
    }

    @Override
    public void update(Comment comment) {
        jdbcTemplate.update("UPDATE comments SET contents=? WHERE seq=?",
                comment.getContents(),
                comment.getSeq()
        );
    }

    @Override
    public Optional<Comment> findById(Long seq) {
        List<Comment> results = jdbcTemplate.query("SELECT c.*,u.email,u.name FROM comments c JOIN users u ON c.user_seq=u.seq WHERE c.seq=?",
                new Object[]{seq},
                mapper
        );
        return ofNullable(results.isEmpty() ? null : results.get(0));
    }

    @Override
    public List<Comment> findAll(Id<Post, Long> postId) {
        // TODO comment 목록 조회
        return jdbcTemplate.query("SELECT c.*,u.email,u.name FROM comments c JOIN users u ON c.user_seq=u.seq WHERE c.post_seq=? ORDER BY c.seq DESC",
                new Object[]{postId.getValue()},
                mapper
        );
    }

    static RowMapper<Comment> mapper = (rs, rowNum) -> Comment.builder()
            .seq(rs.getLong("seq"))
            //.userId(Id.of(User.class, rs.getLong("user_seq")))
            //.postId(Id.of(Post.class, rs.getLong("post_seq")))
            .contents(rs.getString("contents"))
            .writer(new Writer(new Email(rs.getString("email")), rs.getString("name")))
            .createAt(dateTimeOf(rs.getTimestamp("create_at")))
            .build();

}
*/
