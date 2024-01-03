package com.github.steanky.toolkit.function;

import java.util.function.Function;

/**
 * A {@link Function}-like interface that may throw an exception when applying.
 *
 * @param <T> the first input type
 * @param <U> the second input type
 * @param <R> the return type
 * @param <E> the exception type
 */
@FunctionalInterface
public interface ThrowingBiFunction<T, U, R, E extends Exception> {
    /**
     * Calls this function with the provided input.
     *
     * @param t the first input object
     * @param u the second input object
     * @return the output object
     * @throws E if a return value cannot be computed
     */
    R apply(T t, U u) throws E;
}
