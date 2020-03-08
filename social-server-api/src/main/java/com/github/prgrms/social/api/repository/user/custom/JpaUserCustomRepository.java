package com.github.prgrms.social.api.repository.user.custom;

import com.github.prgrms.social.api.model.user.Email;
import com.github.prgrms.social.api.model.user.User;

import java.util.Optional;

public interface JpaUserCustomRepository {

    Optional<User> findByIdCustom(Long id);

    Optional<User> findByEmail(Email email);
}
