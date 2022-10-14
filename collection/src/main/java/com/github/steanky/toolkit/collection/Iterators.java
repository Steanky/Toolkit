package com.github.steanky.toolkit.collection;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Contains utility methods relating to {@link Iterator}, {@link Iterable}, and {@link Collection}.
 */
public final class Iterators {
    private static final Iterator<Object> EMPTY_ITERATOR = new Iterator<>() {
        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public Object next() {
            throw new NoSuchElementException();
        }
    };

    private static final Iterable<?> EMPTY_ITERABLE = () -> EMPTY_ITERATOR;

    /**
     * Returns the common empty {@link Iterator}.
     *
     * @return the common empty iterator
     * @param <T> the element type of the iterator
     */
    @SuppressWarnings("unchecked")
    public static <T> @NotNull Iterator<T> iterator() {
        return (Iterator<T>) EMPTY_ITERATOR;
    }

    /**
     * Returns the common empty {@link Iterable}.
     *
     * @return the common empty iterable
     * @param <T> the element type of the iterable
     */
    @SuppressWarnings("unchecked")
    public static <T> @NotNull Iterable<T> iterable() {
        return (Iterable<T>) EMPTY_ITERABLE;
    }

    /**
     * Returns an {@link Iterator} which iterates the single, provided element.
     *
     * @param element the element to iterate
     * @return a new iterator containing a single value
     * @param <T> the type of value held in the iterator
     */
    public static <T> @NotNull Iterator<T> iterator(T element) {
        return new SingletonIterator<>(element);
    }

    /**
     * Produces an {@link Iterator} from the provided elements.
     *
     * @param elements the elements array
     * @return an iterator over the elements
     * @param <T> the type of element to store in the iterator
     */
    @SafeVarargs
    public static <T> @NotNull Iterator<T> iterator(T ... elements) {
        if (elements.length == 0) {
            //return the empty iterator if possible
            return iterator();
        }

        if (elements.length == 1) {
            //return a singleton iterator containing the only element of this array
            return new SingletonIterator<>(elements[0]);
        }

        return new ArrayIterator<>(elements);
    }

    /**
     * Returns a {@link Iterable} which iterates a single, provided element.
     *
     * @param element the element to iterate
     * @return a new iterable containing a single value
     * @param <T> the type of value held in the iterable
     */
    public static <T> @NotNull Iterable<T> iterable(T element) {
        return () -> new SingletonIterator<>(element);
    }

    /**
     * Produces an {@link Iterable} from the provided elements.
     *
     * @param elements the elements array
     * @return an iterable over the elements
     * @param <T> the type of element to store in the iterable
     */
    @SafeVarargs
    public static <T> @NotNull Iterable<T> iterable(T ... elements) {
        if (elements.length == 0) {
            return iterable();
        }

        if (elements.length == 1) {
            return () -> new SingletonIterator<>(elements[0]);
        }

        return () -> new ArrayIterator<>(elements);
    }

    /**
     * Adds all elements contained in the given {@link Iterable} to the collection. If the iterable is an instance of
     * {@link Collection}, its elements are added using the collection's addAll method. Otherwise, the iterable is
     * iterated and each element added individually.
     *
     * @param iterable the iterable to add to the collection
     * @param collection the collection to which the iterable's contents will be added
     * @param <T> the upper bounds of the iterable's element type
     */
    @SuppressWarnings("unchecked")
    public static <T> void addAll(@NotNull Iterable<? extends T> iterable, @NotNull Collection<? super T> collection) {
        if (iterable instanceof Collection<?>) {
            collection.addAll((Collection<? extends T>) iterable);
        }
        else {
            for (T element : iterable) {
                collection.add(element);
            }
        }
    }

    private static final class SingletonIterator<T> implements Iterator<T> {
        private T element;
        boolean iterated;

        private SingletonIterator(T element) {
            this.element = element;
        }

        @Override
        public boolean hasNext() {
            return !iterated;
        }

        @Override
        public T next() {
            if (iterated) {
                throw new NoSuchElementException();
            }

            iterated = true;
            T elementCopy = element;
            element = null;
            return elementCopy;
        }
    }

    private static final class ArrayIterator<T> implements Iterator<T> {
        private final T[] array;
        private int i;

        private ArrayIterator(T @NotNull [] array) {
            this.array = array;
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
}