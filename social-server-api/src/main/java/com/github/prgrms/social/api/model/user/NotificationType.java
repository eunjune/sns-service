package com.github.prgrms.social.api.model.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public enum NotificationType {
    FOLLOW, COMMENT, LIKE, RETWEET
}
