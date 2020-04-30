package com.github.prgrms.social.api.model.api.request.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequest {

    private String name;

    private String password;

    private boolean isPrivate;

    public void encode(PasswordEncoder passwordEncoder) {
        if(password != null && !password.isEmpty()) {
            password = passwordEncoder.encode(password);
        }
    }
}
