package com.github.steanky.toolkit.collection;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Contains utility methods relating to {@link Iterator}, {@link Iterable}, and {@link Collection}.
 */
public final class CollectionUtils {
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
    public static <T> @NotNull Iterator<T> emptyIterator() {
        return (Iterator<T>) EMPTY_ITERATOR;
    }

    /**
     * Returns the common empty {@link Iterable}.
     *
     * @return the common empty iterable
     * @param <T> the element type of the iterable
     */
    @SuppressWarnings("unchecked")
    public static <T> @NotNull Iterable<T> emptyIterable() {
        return (Iterable<T>) EMPTY_ITERABLE;
    }

    /**
     * Returns an {@link Iterator} which iterates the single, provided element.
     *
     * @param element the element to iterate
     * @return a new iterator containing a single value
     * @param <T> the type of value held in the iterator
     */
    public static <T> @NotNull Iterator<T> singletonIterator(T element) {
        return new SingletonIterator<>(element);
    }

    /**
     * Returns a {@link Iterable} which iterates a single, provided element.
     *
     * @param element the element to iterate
     * @return a new iterable containing a single value
     * @param <T> the type of value held in the iterable
     */
    public static <T> @NotNull Iterable<T> singletonIterable(T element) {
        return () -> singletonIterator(element);
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

    private static final class SingletonIterator<T>implements Iterator<T> {
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
}
