package com.github.prgrms.social.api.repository.user;

import com.github.prgrms.social.api.model.user.Email;
import com.github.prgrms.social.api.model.user.User;
import com.github.prgrms.social.api.repository.user.custom.JpaUserCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<User,Long>, JpaUserCustomRepository {

    User save(User user);

    @Override
    @Transactional(readOnly = true)
    Optional<User> findByIdCustom(Long id);

    @Override
    @Transactional(readOnly = true)
    Optional<User> findByEmail(Email email);
}
