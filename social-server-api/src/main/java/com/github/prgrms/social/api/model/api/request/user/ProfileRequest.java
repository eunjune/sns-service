package com.github.prgrms.social.api.model.api.request.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequest {

    private String name;

    private String password;

    private boolean isPrivate;
}
