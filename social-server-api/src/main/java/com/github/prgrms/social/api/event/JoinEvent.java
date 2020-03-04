package com.github.prgrms.social.api.event;

import com.github.prgrms.social.api.model.commons.Id;
import com.github.prgrms.social.api.model.user.User;
import lombok.Getter;
import lombok.ToString;

// 카프카에 전송하기 위한 회원가입 이벤트 객체
@Getter
@ToString
public class JoinEvent {

    private final Id<User, Long> userId;

    private final String name;

    public JoinEvent(User user) {
        this.userId = Id.of(User.class, user.getId());
        this.name = user.getName();
    }
}
