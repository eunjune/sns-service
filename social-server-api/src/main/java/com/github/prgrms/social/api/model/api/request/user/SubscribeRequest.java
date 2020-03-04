package com.github.prgrms.social.api.model.api.request.user;

import com.github.prgrms.social.api.model.user.Subscription;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// Subscribe 요청 데이터를 받는 DTO
@Getter
@Setter
@ToString
public class SubscribeRequest {

    private String publicKey;

    private String auth;

    private String notificationEndPoint;

    public Subscription newSubscription(Long userSeq, String endpoint, String publicKey, String auth) {
        return Subscription.builder()
                .userId(userSeq)
                .endpoint(endpoint)
                .publicKey(publicKey)
                .auth(auth)
                .build();
    }
}
