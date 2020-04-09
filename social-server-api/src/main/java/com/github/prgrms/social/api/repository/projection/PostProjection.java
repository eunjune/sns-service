package com.github.prgrms.social.api.repository.projection;

import com.github.prgrms.social.api.model.user.User;

public interface PostProjection {

    public Long getId();

    public User getUser();

}
