package com.github.steanky.toolkit.collection;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Contains utility methods relating to {@link Iterator}.
 */
public final class Iterators {
    /**
     * Returns the common empty {@link Iterator}.
     *
     * @return the common empty iterator
     * @param <T> the element type of the iterator
     */
    public static <T> @NotNull Iterator<T> iterator() {
        return Collections.emptyIterator();
    }

    /**
     * Returns an {@link Iterator} which iterates the single, provided element. It will be immutable.
     *
     * @param element the element to iterate
     * @return a new iterator containing a single value
     * @param <T> the type of value held in the iterator
     */
    public static <T> @NotNull Iterator<T> iterator(T element) {
        return new SingletonIterator<>(element);
    }

    /**
     * Produces an {@link Iterator} from the provided elements. It will be immutable.
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

        return new ArrayIterator<>(elements, 0);
    }

    /**
     * Returns the common empty {@link ListIterator}. It will be immutable.
     *
     * @return the common empty list iterator
     * @param <T> the element type of the iterator
     */
    public static <T> @NotNull ListIterator<T> listIterator() {
        return Collections.emptyListIterator();
    }

    /**
     * Returns a {@link ListIterator} which iterates the single, provided element. It will be immutable.
     *
     * @param element the element to iterate
     * @return a new iterator containing a single value
     * @param <T> the type of value held in the iterator
     */
    public static <T> @NotNull ListIterator<T> listIterator(T element) {
        return new SingletonListIterator<>(element);
    }

    /**
     * Produces an {@link ListIterator} from the provided elements. It will be immutable, but changes to the given
     * elements array will be reflected in the iterator.
     *
     * @param elements the elements array
     * @return an iterator over the elements
     * @param <T> the type of element to store in the iterator
     */
    @SafeVarargs
    public static <T> @NotNull ListIterator<T> listIterator(T ... elements) {
        if (elements.length == 0) {
            //return the empty iterator if possible
            return listIterator();
        }

        if (elements.length == 1) {
            //return a singleton iterator containing the only element of this array
            return new SingletonListIterator<>(elements[0]);
        }

        return new ArrayListIterator<>(elements, 0);
    }

    /**
     * Returns a {@link ListIterator} which iterates the single, provided element. The element may be changed using
     * {@link ListIterator#set(Object)}.
     *
     * @param element the element to iterate
     * @return a new iterator containing a single value
     * @param <T> the type of value held in the iterator
     */
    public static <T> @NotNull ListIterator<T> mutableListIterator(T element) {
        return new MutableSingletonListIterator<>(element);
    }

    /**
     * Produces an {@link ListIterator} from the provided elements. It will be immutable. The element(s) (if length is
     * non-zero) may be changed using {@link ListIterator#set(Object)}. Any changes made this way will be visible in the
     * given array. Likewise, any changes in the array will be visible in the iterator.
     *
     * @param elements the elements array
     * @return an iterator over the elements
     * @param <T> the type of element to store in the iterator
     */
    @SafeVarargs
    public static <T> @NotNull ListIterator<T> mutableListIterator(T ... elements) {
        if (elements.length == 0) {
            //return the empty iterator if possible
            return listIterator();
        }

        return new MutableArrayListIterator<>(elements, 0);
    }

    /**
     * Converts a potentially modifiable iterator (one which supports the {@link Iterator#remove()} method) to one that
     * does not.
     *
     * @param iterator the iterator to wrap
     * @return an unmodifiable iterator
     * @param <T> the type of object held in the iterator
     */
    public static <T> @NotNull Iterator<T> unmodifiableIterator(@NotNull Iterator<T> iterator) {
        Objects.requireNonNull(iterator);

        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public T next() {
                return iterator.next();
            }
        };
    }

    static class SingletonIterator<T> implements Iterator<T> {
        protected T element;
        protected boolean iterated;

        SingletonIterator(T element) {
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
            return element;
        }
    }

    static class SingletonListIterator<T> extends SingletonIterator<T> implements ListIterator<T> {
        SingletonListIterator(T element) {
            super(element);
        }

        @Override
        public boolean hasPrevious() {
            return iterated;
        }

        @Override
        public T previous() {
            if (!iterated) {
                throw new NoSuchElementException();
            }

            return element;
        }

        @Override
        public int nextIndex() {
            return iterated ? 1 : 0;
        }

        @Override
        public int previousIndex() {
            return iterated ? 0 : -1;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(T t) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void add(T t) {
            throw new UnsupportedOperationException();
        }
    }

    static class MutableSingletonListIterator<T> extends SingletonListIterator<T> {
        MutableSingletonListIterator(T element) {
            super(element);
        }

        @Override
        public void set(T t) {
            if (!iterated) {
                throw new IllegalStateException();
            }

            element = t;
        }
    }

    static class ArrayIterator<T> implements Iterator<T> {
        protected final T[] array;
        protected int cursor;

        ArrayIterator(T[] array, int cursor) {
            this.array = array;
            this.cursor = cursor;
        }

        @Override
        public boolean hasNext() {
            return cursor < array.length;
        }

        @Override
        public T next() {
            return array[cursor++];
        }
    }

    static class ArrayListIterator<T> extends ArrayIterator<T> implements ListIterator<T> {
        ArrayListIterator(T[] array, int cursor) {
            super(array, cursor);
        }

        @Override
        public boolean hasPrevious() {
            return cursor != 0;
        }

        @Override
        public T previous() {
            return array[--cursor];
        }

        @Override
        public int nextIndex() {
            return cursor;
        }

        @Override
        public int previousIndex() {
            return cursor - 1;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(T t) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void add(T t) {
            throw new UnsupportedOperationException();
        }
    }

    static final class MutableArrayListIterator<T> extends ArrayListIterator<T> {
        MutableArrayListIterator(T[] array, int cursor) {
            super(array, cursor);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(T t) {
            array[cursor] = t;
        }

        @Override
        public void add(T t) {
            throw new UnsupportedOperationException();
        }
    }
}