package com.github.prgrms.social.push.repository;

import com.github.prgrms.social.api.model.user.Subscription;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import static com.github.prgrms.social.push.util.DateTimeUtils.dateTimeOf;
import static com.github.prgrms.social.push.util.DateTimeUtils.timestampOf;

@Repository
public class JdbcPushRepository implements PushRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcPushRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Subscription save(Subscription subscription) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement("MERGE INTO subscriptions(seq,user_seq,endpoint,public_key,auth,create_at) " +
                    "KEY(user_seq) VALUES (null,?,?,?,?,?)", new String[]{"seq"});
            ps.setLong(1, subscription.getUserSeq());
            ps.setString(2, subscription.getEndpoint());
            ps.setString(3, subscription.getPublicKey());
            ps.setString(4, subscription.getAuth());
            ps.setTimestamp(5, timestampOf(subscription.getCreateAt()));
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        long generatedSeq = key != null ? key.longValue() : -1;

        return Subscription.builder()
                .seq(generatedSeq)
                .endpoint(subscription.getEndpoint())
                .publicKey(subscription.getPublicKey())
                .auth(subscription.getAuth())
                .createAt(subscription.getCreateAt())
                .build();
    }

    @Override
    public Optional<Subscription> findByUserId(Long postWriterSeq) {
        List<Subscription> results = jdbcTemplate.query(
                "SELECT * FROM subscriptions WHERE user_seq = ?",
                new Object[]{String.valueOf(postWriterSeq)},
                mapper
        );
        return Optional.ofNullable(results.isEmpty() ? null : results.get(0));

    }

    static RowMapper<Subscription> mapper = (rs, rowNum) -> Subscription.builder()
            .seq(rs.getLong("seq"))
            .userSeq(rs.getLong("user_seq"))
            .endpoint(rs.getString("endpoint"))
            .publicKey(rs.getString("public_key"))
            .auth(rs.getString("auth"))
            .createAt(dateTimeOf(rs.getTimestamp("create_at")))
            .build();


}
