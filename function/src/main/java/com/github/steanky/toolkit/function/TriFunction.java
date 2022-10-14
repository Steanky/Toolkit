package com.github.steanky.toolkit.function;

/**
 * A function that accepts three values and returns something.
 *
 * @param <T> the first parameter type
 * @param <U> the second parameter type
 * @param <V> the third parameter type
 * @param <R> the return type
 */
@FunctionalInterface
public interface TriFunction<T, U, V, R> {
    /**
     * Computes the result of this function.
     *
     * @param a the first argument
     * @param b the second argument
     * @param c the third argument
     * @return the return value
     */
    R apply(T a, U b, V c);
}
