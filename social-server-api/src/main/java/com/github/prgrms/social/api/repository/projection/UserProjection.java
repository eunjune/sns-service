package com.github.prgrms.social.api.repository.projection;

import com.github.prgrms.social.api.model.user.User;

import java.util.List;

public interface UserProjection {

    Boolean getIsPrivate();

    List<User> getFollowers();
}
