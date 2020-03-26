package com.github.prgrms.social.api.service.user;

import com.github.prgrms.social.api.model.user.User;

public interface EmailService {

    public void sendMessage(User user);

}
