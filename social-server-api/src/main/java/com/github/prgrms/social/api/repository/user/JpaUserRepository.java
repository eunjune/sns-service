package com.github.prgrms.social.api.repository.user;

import com.github.prgrms.social.api.model.user.Email;
import com.github.prgrms.social.api.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<User,Long>{

    User save(User user);

    @Transactional(readOnly = true)
    Optional<User> findById(Long id);

    @Transactional(readOnly = true)
    Optional<User> findByEmail(Email email);

    @Transactional(readOnly = true)
    boolean existsByEmail(Email email);

    @Transactional(readOnly = true)
    boolean existsByName(String name);
}
