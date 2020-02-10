package com.github.prgrms.social.api.model.commons;

import lombok.*;

import javax.persistence.Embeddable;

import static com.google.common.base.Preconditions.checkNotNull;

@Embeddable
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true)
@Getter
@EqualsAndHashCode(of = {"reference","value"})
@ToString
public class Id<R, V> {

    private final Class<R> reference;

    private final V value;

    public static <R, V> Id<R, V> of(Class<R> reference, V value) {
        checkNotNull(reference, "reference must be provided.");
        checkNotNull(value, "value must be provided.");

        return new Id<>(reference, value);
    }
}
