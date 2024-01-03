package com.github.steanky.toolkit.function;

import java.util.function.Consumer;

/**
 * A {@link Consumer}-like interface that may throw an exception when accepting a value.
 *
 * @param <T> the value to accept
 * @param <E> the exception type
 */
@FunctionalInterface
public interface ThrowingConsumer<T, E extends Exception> {
    /**
     * Calls this consumer with the provided input.
     *
     * @param t the input object
     * @throws E if a return value cannot be computed
     */
    void accept(T t) throws E;
}
