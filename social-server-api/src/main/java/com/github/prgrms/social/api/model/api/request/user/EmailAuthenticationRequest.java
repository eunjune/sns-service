package com.github.prgrms.social.api.model.api.request.user;

import lombok.Data;

@Data
public class EmailAuthenticationRequest {

    private String emailToken;

    private String email;
}
