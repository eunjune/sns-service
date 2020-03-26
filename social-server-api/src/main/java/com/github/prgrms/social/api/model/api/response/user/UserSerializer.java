package com.github.prgrms.social.api.model.api.response.user;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.prgrms.social.api.model.user.User;

import java.io.IOException;

public class UserSerializer extends StdSerializer<User> {

    public UserSerializer() {
        this(null);
    }

    protected UserSerializer(Class<User> t) {
        super(t);
    }

    @Override
    public void serialize(User value, JsonGenerator gen, SerializerProvider provider) throws IOException {

        gen.writeStartObject();
        gen.writeNumberField("id", value.getId());
        gen.writeStringField("name",value.getName());
        gen.writeStringField("email",value.getEmail().getAddress());
        gen.writeNumberField("loginCount", value.getLoginCount());
        gen.writeBooleanField("isEmailCertification", value.isEmailCertification());
        gen.writeStringField("createAt", value.getCreateAt().toString());

        if(value.getLastLoginAt().isPresent()) {
            gen.writeStringField("lastLoginAt", value.getLastLoginAt().orElse(null).toString());
        }

        if(value.getProfileImageUrl().isPresent()) {
            gen.writeStringField("profileImageUrl",value.getProfileImageUrl().orElse(null));
        }

        long[] followings = new long[value.getConnectedUsers().size()];
        for(int i=0; i<value.getConnectedUsers().size(); ++i) {
            followings[i] = value.getConnectedUsers().get(i).getTargetUser().getId();
        }
        gen.writeFieldName("followings");
        gen.writeArray(followings, 0, followings.length);

        long[] followers = new long[0];
        gen.writeFieldName("followers");
        gen.writeArray(followers,0,followers.length);

        gen.writeEndObject();

    }
}
