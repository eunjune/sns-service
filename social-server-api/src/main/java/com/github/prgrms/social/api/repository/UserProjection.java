package com.github.prgrms.social.api.repository;

import com.github.prgrms.social.api.model.user.User;

import java.util.Set;

public interface UserProjection {

    Set<User> getFollowers();

    Boolean getIsEmailCertification();

    Boolean getIsPrivate();

}
