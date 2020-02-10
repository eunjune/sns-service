package com.github.prgrms.social.api.model.post;

import com.github.prgrms.social.api.model.user.Email;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Optional;

import static java.util.Optional.ofNullable;

@AllArgsConstructor
@Getter
@ToString
public class Writer {

    @ApiModelProperty(value = "이메일", required = true)
    private final Email email;

    @ApiModelProperty(value = "이름")
    private final String name;

    public Writer(Email email) {
        this(email, null);
    }

    public Optional<String> getName() {
        return ofNullable(name);
    }
}
