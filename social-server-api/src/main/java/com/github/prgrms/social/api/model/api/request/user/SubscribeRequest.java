package com.github.prgrms.social.api.model.api.request.user;

import com.github.prgrms.social.api.model.user.Subscription;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

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
                .userSeq(userSeq)
                .endpoint(endpoint)
                .publicKey(publicKey)
                .auth(auth)
                .build();
    }

    /* public String getPublicKey() {
            return publicKey;
        }

        public void setPublicKey(String publicKey) {
            this.publicKey = publicKey;
        }

        public String getAuth() {
            return auth;
        }

        public void setAuth(String auth) {
            this.auth = auth;
        }

        public String getNotificationEndPoint() {
            return notificationEndPoint;
        }

        public void setNotificationEndPoint(String notificationEndPoint) {
            this.notificationEndPoint = notificationEndPoint;
        }
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("publicKey", publicKey)
                .append("auth", auth)
                .append("notificationEndPoint", notificationEndPoint)
                .toString();
    }*/
}
