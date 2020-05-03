package com.github.prgrms.social.api.repository.user;

import com.github.prgrms.social.api.model.user.Email;
import com.github.prgrms.social.api.model.user.User;
import com.github.prgrms.social.api.repository.projection.UserProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    User save(User user);

    @Transactional(readOnly = true)
    Optional<User> findById(Long id);

    @Transactional(readOnly = true)
    @EntityGraph(attributePaths = {"followings","followers"})
    Optional<User> findWithUserAllById(Long id);

    @Transactional(readOnly = true)
    @EntityGraph(attributePaths = {"followings","followers","posts","notifications"})
    Optional<User> findUserWithUserWithPostById(Long id);

    @Transactional(readOnly = true)
    Optional<User> findByEmail(Email email);

    @Transactional(readOnly = true)
    Optional<User> findByName(String name);

    @Transactional(readOnly = true)
    boolean existsByEmail(Email email);

    @Transactional(readOnly = true)
    boolean existsByName(String name);

    @Transactional(readOnly = true)
    @Query(value = "SELECT u2.* FROM users u1 " +
            "LEFT OUTER JOIN connected_user c ON u1.id = c.follower_id " +
            "LEFT OUTER JOIN users u2 ON c.following_id = u2.id " +
            "WHERE u1.id = :id", nativeQuery = true)
    List<User> findFollowingsById(@Param("id") Long id, Pageable pageable);

    @Transactional(readOnly = true)
    @Query(value = "SELECT u2.* FROM users u1 " +
            "LEFT OUTER JOIN connected_user c ON u1.id = c.following_id " +
            "LEFT OUTER JOIN users u2 ON c.follower_id = u2.id " +
            "WHERE u1.id = :id", nativeQuery = true)
    List<User> findFollowersById(@Param("id") Long id, Pageable pageable); //, @Param("limit") int limit, @Param("offset") long offset

    @Transactional(readOnly = true)
    @EntityGraph(attributePaths = {"followers"})
    UserProjection findFollowersAllById(Long id);

    @Transactional(readOnly = true)
    UserProjection findPrivateById(Long id);

    @Transactional(readOnly = true)
    UserProjection findEmailCertificationByEmail(Email email);
}
