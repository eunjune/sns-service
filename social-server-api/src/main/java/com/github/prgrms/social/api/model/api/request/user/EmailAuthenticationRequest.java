package com.github.prgrms.social.api.model.api.request.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailAuthenticationRequest {

    private String emailToken;

    private String email;
}
