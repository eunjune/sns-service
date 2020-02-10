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
    public Optional<Post> findById(Long seq, Long userSeq, Long postWriterSeq) {

        Post post = (Post)entityManager.createNativeQuery("SELECT p.*, NVL2(l.user_seq,true,false) likes_of_me " +
                "FROM post p JOIN users u ON p.user_seq=u.seq LEFT JOIN likes l ON l.user_seq=:userSeq AND l.post_seq = p.seq " +
                "WHERE p.user_seq=:postWriterSeq AND p.seq=:seq", Post.class)
                .setParameter("userSeq", userSeq)
                .setParameter("postWriterSeq", postWriterSeq)
                .setParameter("seq", seq).getSingleResult();

        post.setLikesOfMe(userSeq);

        return Optional.of(post);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Post> findAll(Long userSeq, Long postWriterSeq, Pageable pageable) {

        List resultList = entityManager.createNativeQuery("SELECT p.*, NVL2(l.user_seq,true,false) likes_of_me " +
                "FROM post p JOIN users u ON p.user_seq=u.seq LEFT JOIN likes l ON l.user_seq=:userSeq AND l.post_seq = p.seq " +
                "WHERE p.user_seq=:postWriterSeq ORDER BY p.seq DESC LIMIT :size OFFSET :offset", Post.class)
                .setParameter("userSeq", userSeq)
                .setParameter("postWriterSeq", postWriterSeq)
                .setParameter("size", pageable.getPageSize())
                .setParameter("offset", pageable.getOffset())
                .getResultList();

        for(int i=0; i<resultList.size(); ++i) {
            Post post = (Post)resultList.get(i);
            post.setLikesOfMe(userSeq);
        }

        return resultList;
    }
}
