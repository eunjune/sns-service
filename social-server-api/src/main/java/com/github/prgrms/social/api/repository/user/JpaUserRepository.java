package com.github.prgrms.social.api.repository.user;

import com.github.prgrms.social.api.model.user.Email;
import com.github.prgrms.social.api.model.user.User;
import com.github.prgrms.social.api.repository.projection.UserProjection;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<User,Long>{

    User save(User user);

    @Transactional(readOnly = true)
    Optional<User> findById(Long id);

    @Transactional(readOnly = true)
    @EntityGraph(attributePaths = {"followings","followers","posts"})
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
    @EntityGraph(attributePaths = {"followings"})
    UserProjection findFollowingsById(Long id);

    @Transactional(readOnly = true)
    @EntityGraph(attributePaths = {"followers"})
    UserProjection findFollowersById(Long id);

    @Transactional(readOnly = true)
    UserProjection findPrivateById(Long id);

    @Transactional(readOnly = true)
    UserProjection findEmailCertificationById(Long Id);


}
