package com.github.steanky.toolkit.collection;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;

/**
 * Contains utility methods relating to {@link Iterator}, {@link Iterable}, and {@link Collection}.
 */
public final class Iterators {
    private static final Iterable<?> EMPTY_ITERABLE = Collections::emptyIterator;

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

    /**
     * Returns a read-only view of the provided array. If the array is modified, the returned list will reflect the
     * changes.
     *
     * @param array the underlying array
     * @return a read-only view of the array
     * @param <T> the component type of the array
     */
    public static <T> @NotNull List<T> arrayView(T @NotNull [] array) {
        Objects.requireNonNull(array);
        if (array.length == 0) {
            return Collections.emptyList();
        }

        return new AbstractList<>() {
            @Override
            public T get(int index) {
                return array[index];
            }

            @Override
            public int size() {
                return array.length;
            }
        };
    }

    /**
     * Creates a mapped view of the given collection. The returned value is read-through. During iteration, the
     * provided function is used to appropriately map the element types.
     *
     * @param mapper the mapper function
     * @param original the original collection
     * @return the mapped view collection
     * @param <T> the upper bounds of the original collection
     * @param <R> the component type of the mapped collection
     */
    public static <T, R> @NotNull Collection<R> mappedView(@NotNull Function<? super T, ? extends R> mapper,
            @NotNull Collection<? extends T> original) {
        Objects.requireNonNull(mapper);
        Objects.requireNonNull(original);

        return new AbstractCollection<>() {
            @Override
            public Iterator<R> iterator() {
                return new Iterator<>() {
                    private final Iterator<? extends T> iterator = original.iterator();

                    @Override
                    public boolean hasNext() {
                        return iterator.hasNext();
                    }

                    @Override
                    public R next() {
                        return mapper.apply(iterator.next());
                    }
                };
            }

            @Override
            public int size() {
                return original.size();
            }
        };
    }

    /**
     * Works the same as {@link Iterators#mappedView(Function, Collection)}, but uses an array instead of a collection.
     *
     * @param mapper the mapper function
     * @param original the original array
     * @return a mapped view of the array
     * @param <T> the upper bounds of the original collection
     * @param <R> the component type of the mapped collection
     */
    public static <T, R> @NotNull List<R> mappedView(@NotNull Function<? super T, ? extends R> mapper,
            @NotNull T[] original) {
        Objects.requireNonNull(mapper);
        Objects.requireNonNull(original);

        return new AbstractList<>() {
            @Override
            public R get(int index) {
                return mapper.apply(original[index]);
            }

            @Override
            public Iterator<R> iterator() {
                return new Iterator<>() {
                    private int i;

                    @Override
                    public boolean hasNext() {
                        return i < original.length;
                    }

                    @Override
                    public R next() {
                        return mapper.apply(original[i++]);
                    }
                };
            }

            @Override
            public int size() {
                return original.length;
            }
        };
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

    private static final class SingletonIterator<T> implements Iterator<T> {
        private final T element;
        private boolean iterated;

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
            return element;
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