package com.github.prgrms.social.api.repository.projection;

import com.github.prgrms.social.api.model.user.User;

public interface PostProjection {

    Long getId();

    User getUser();

}
