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
    /*
    private Id(Class<R> reference, V value) {
        this.reference = reference;
        this.value = value;
    }

    public V value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Id<?, ?> id = (Id<?, ?>) o;
        return Objects.equals(reference, id.reference) &&
                Objects.equals(value, id.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reference, value);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("reference", reference.getSimpleName())
                .append("value", value)
                .toString();
    }
*/
}
