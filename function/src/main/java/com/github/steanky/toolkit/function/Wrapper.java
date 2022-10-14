package com.github.steanky.toolkit.function;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * A mutable container for a single object. A standard implementation can be obtained through {@link Wrapper#of(Object)}.
 * @param <T> the type of object held in the wrapper
 */
public interface Wrapper<T> extends Supplier<T> {
    /**
     * Sets the value underlying this wrapper.
     *
     * @param newValue the wrapper's new value
     */
    void set(T newValue);

    /**
     * Converts this Wrapper to an {@link Optional}, using its current value. The Optional will be empty if this Wrapper
     * holds {@code null}.
     * @return an optional containing the value of this Wrapper, or an empty optional if it contains null
     */
    default @NotNull Optional<T> toOptional() {
        return Optional.ofNullable(get());
    }

    /**
     * Creates a new Wrapper using the provided initial value.
     *
     * @param initialValue the initial value
     * @return a new Wrapper holding the initial value
     * @param <T> the value type
     */
    static <T> @NotNull Wrapper<T> of(T initialValue) {
        return new BasicWrapper<>(initialValue);
    }

    /**
     * Creates a new Wrapper using an initial value of {@code null}.
     *
     * @return a new Wrapper holding a {@code null} initial value
     * @param <T> the value type
     */
    static <T> @NotNull Wrapper<T> ofNull() {
        return new BasicWrapper<>(null);
    }
}
