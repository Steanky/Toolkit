package com.github.steanky.toolkit.collection;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * An {@link Iterator} over a simple array.
 *
 * @param <T> the component type of the array
 */
public final class ArrayIterator<T> implements Iterator<T> {
    private final T[] array;
    private int i;

    /**
     * Creates a new instance of this iterator from the provided array.
     *
     * @param array the array to iterate
     */
    public ArrayIterator(T @NotNull [] array) {
        this.array = Objects.requireNonNull(array);
    }

    @Override
    public boolean hasNext() {
        return i < array.length;
    }

    @Override
    public T next() {
        if (i >= array.length) {
            throw new NoSuchElementException("Array index " + i + " out of bounds for length " + array.length);
        }

        return array[i++];
    }
}
