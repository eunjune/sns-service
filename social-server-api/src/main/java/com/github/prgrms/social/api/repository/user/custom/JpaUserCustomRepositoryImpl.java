package com.github.prgrms.social.api.repository.user.custom;

import com.github.prgrms.social.api.model.user.ConnectedUser;
import com.github.prgrms.social.api.model.user.Email;
import com.github.prgrms.social.api.model.user.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
@Transactional
public class JpaUserCustomRepositoryImpl implements JpaUserCustomRepository{

    @PersistenceContext
    EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByIdCustom(Long id) {

        User user = entityManager.find(User.class, id);

        for(ConnectedUser connectedUser : user.getConnectedUsers()) {
            user.addFollowing(connectedUser.getTargetUser().getId());
        }

        return Optional.of(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(Email email) {
        User user = (User)entityManager.createNativeQuery("SELECT u.* FROM users u WHERE u.email = :email", User.class)
                .setParameter("email",email.getAddress());

        for(ConnectedUser connectedUser : user.getConnectedUsers()) {
            user.addFollowing(connectedUser.getTargetUser().getId());
        }

        return Optional.of(user);
    }

}
