package com.github.prgrms.social.api.model.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.Embeddable;

import static java.util.regex.Pattern.matches;

@Embeddable
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = "address")
@ToString
public class Email {

    @ApiModelProperty(value = "이메일 주소", required = true)
    private final String address;

    public static boolean checkAddress(String address) {
        return matches("[\\w~\\-.+]+@[\\w~\\-]+(\\.[\\w~\\-]+)+", address);
    }

    public String getName() {
        String[] tokens = address.split("@");
        if (tokens.length == 2)
            return tokens[0];
        return null;
    }

    public String getDomain() {
        String[] tokens = address.split("@");
        if (tokens.length == 2)
            return tokens[1];
        return null;
    }
}
