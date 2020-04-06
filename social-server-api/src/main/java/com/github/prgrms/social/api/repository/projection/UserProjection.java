package com.github.prgrms.social.api.repository.projection;

import com.github.prgrms.social.api.model.user.User;

import java.util.Set;

public interface UserProjection {

    Set<User> getFollowers();

    Set<User> getFollowings();

    Boolean getIsEmailCertification();

    Boolean getIsPrivate();

}
