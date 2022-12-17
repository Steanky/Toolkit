package com.github.steanky.toolkit.collection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * <p>A simple mutable container object which may contain some value, or {@code null}. This is primarily useful when
 * attempting to pass values outside the scope of a lambda expression, or to simulate pass-by-reference semantics.
 * {@link Optional}, though similar in some ways, cannot be used for this purpose as it is shallowly immutable.
 * {@link AtomicReference} is mutable but is primarily designed for use in a multithreaded context. Single-length
 * arrays can be used but lack convenience methods and interoperability with Java utility classes.</p>
 *
 * <p>Wrapper instances are considered equal if and only if the objects they contain are equal. Likewise, a wrapper's
 * hashcode is equal to the hashcode of the stored object, or 0 if {@code null}.</p>
 *
 * <p>Wrapper instances can be obtained through the static factory method {@link Wrapper#of(Object)} or
 * {@link Wrapper#ofNull()}.</p>
 *
 * <p>Wrappers can also be treated as immutable-length {@link List}s with a size of 1, whose single element may be
 * replaced but never removed. Correspondingly, methods like {@link List#add(Object)}, {@link List#remove(int)}
 * will generally not be implemented (because they imply that the length of the list is changed).</p>
 * The object stored in the wrapper may be changed using {@link Wrapper#set(Object)}.
 * @param <T> the type of object held in the wrapper
 */
public interface Wrapper<T> extends Supplier<T>, List<T> {
    /**
     * Sets the value underlying this wrapper.
     *
     * @param newValue the wrapper's new value
     * @return the new value
     */
    T set(@Nullable T newValue);

    /**
     * Converts this Wrapper to an {@link Optional}, using its current value. The Optional will be empty if this Wrapper
     * holds {@code null}.
     * @return an optional containing the value of this Wrapper, or an empty optional if it contains null
     */
    default @NotNull Optional<T> toOptional() {
        return Optional.ofNullable(get());
    }

    /**
     * Applies a mapping function to this wrapper's value, and returns a new wrapper containing the result of this
     * function. The contents of this wrapper are not changed.
     *
     * @param mapper the mapping function to apply
     * @param <U> the new value type
     * @return a new wrapper holding the mapped value
     */
    default <U> @NotNull Wrapper<U> map(@NotNull Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        return new BasicWrapper<>(mapper.apply(get()));
    }

    /**
     * Applies a mapping function to this wrapper's value. The result of the mapping function will be the new value held
     * by this wrapper, and is returned.
     *
     * @param mapper the mapping function to apply
     * @return the new value
     */
    default T apply(@NotNull Function<? super T, ? extends T> mapper) {
        Objects.requireNonNull(mapper);
        return set(mapper.apply(get()));
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
