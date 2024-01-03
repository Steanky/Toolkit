package com.github.steanky.toolkit.function;

import java.util.function.BiConsumer;

/**
 * A {@link BiConsumer}-like interface that may throw an exception when accepting values
 *
 * @param <T> the first value to accept
 * @param <U> the second value to accept
 * @param <E> the exception type
 */
@FunctionalInterface
public interface ThrowingBiConsumer<T, U, E extends Exception> {
    /**
     * Calls this consumer with the provided input objects.
     *
     * @param t the first input object
     * @param u the second input object
     * @throws E if a return value cannot be computed
     */
    void accept(T t, U u) throws E;
}
