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

    private static boolean checkAddress(String address) {
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
/*

    public Email(String address) {
        checkArgument(isNotEmpty(address), "address must be provided.");
        checkArgument(
                address.length() >= 4 && address.length() <= 50,
                "address length must be between 4 and 50 characters."
        );
        checkArgument(checkAddress(address), "Invalid email address: " + address);

        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(address, email.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("address", address)
                .toString();
    }
*/

}
