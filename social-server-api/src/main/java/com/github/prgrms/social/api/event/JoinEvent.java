package com.github.prgrms.social.api.event;

import com.github.prgrms.social.api.model.user.User;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class JoinEvent {

    private final Long id;

    private final String name;

    public JoinEvent(User user) {
        this.id = user.getId();
        this.name = user.getName();
    }
}
