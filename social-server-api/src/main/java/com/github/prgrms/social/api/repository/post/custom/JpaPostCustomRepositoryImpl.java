package com.github.prgrms.social.api.repository.post.custom;

import com.github.prgrms.social.api.model.post.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class JpaPostCustomRepositoryImpl implements JpaPostCustomRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public Optional<Post> findByIdCustom(Long id, Long userId, Long postWriterId) {

        Post post = (Post)entityManager.createNativeQuery("SELECT DISTINCT p.*, " +
                "CASE WHEN l.user_id = NULL THEN false ELSE true END AS likes_of_me, " +
                "CASE WHEN r.target_post_id = NULL THEN false ELSE true END AS is_retweet " +
                "FROM post p JOIN users u ON p.user_id=u.id LEFT JOIN likes l ON l.user_id=:userId AND l.post_id = p.id " +
                "LEFT JOIN image i ON p.id=i.post_id " +
                "LEFT JOIN retweet r on p.id = r.post_id " +
                "WHERE p.user_id=:postWriterId AND p.id=:id", Post.class)
                .setParameter("userId", userId)
                .setParameter("postWriterId", postWriterId)
                .setParameter("id", id).getSingleResult();

        post.setLikesOfMe(userId);

        return Optional.of(post);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Post> findAllById(Long userId, Long postWriterId, Pageable pageable) {
        List resultList = entityManager.createNativeQuery("SELECT DISTINCT p.*, " +
                "CASE WHEN l.user_id = NULL THEN false ELSE true END AS likes_of_me, " +
                "CASE WHEN r.post_id = NULL THEN false ELSE true END AS is_retweet " +
                "FROM post p JOIN users u ON p.user_id=u.id LEFT JOIN likes l ON l.user_id=:userId AND l.post_id = p.id " +
                "LEFT JOIN image i ON p.id=i.post_id " +
                "LEFT JOIN retweet r on p.id = r.post_id " +
                "WHERE p.user_id=:postWriterId ORDER BY p.create_at DESC LIMIT :size OFFSET :offset", Post.class)
                .setParameter("userId", userId)
                .setParameter("postWriterId", postWriterId)
                .setParameter("size", pageable.getPageSize())
                .setParameter("offset", pageable.getOffset())
                .getResultList();

        for(int i=0; i<resultList.size(); ++i) {
            Post post = (Post)resultList.get(i);
            post.setLikesOfMe(userId);
            post.setIsRetweet();
        }

        return resultList;
    }
}
