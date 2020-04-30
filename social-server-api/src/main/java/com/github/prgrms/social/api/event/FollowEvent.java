package com.github.prgrms.social.api.event;

import com.github.prgrms.social.api.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FollowEvent {

    private User me;

    private User targetUser;

}
