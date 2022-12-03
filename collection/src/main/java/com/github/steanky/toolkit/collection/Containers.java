package com.github.steanky.toolkit.collection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Stream;

public final class Containers {

    /**
     * Returns a read-only view of the provided array. If the array is modified, the returned list will reflect the
     * changes.
     * <p>
     * The returned list cannot be modified (other than by modifying the array). It is designed to act as an extremely
     * lightweight wrapper around the array, and most operations (when possible) interact directly with the array. In
     * particular, any of its iterators may throw {@link IndexOutOfBoundsException} instead of
     * {@link NoSuchElementException} if {@link Iterator#next()} is called more times than the length of the array.
     * <p>
     * The length of the list may never change.
     * @param array the underlying array
     * @return a read-only view of the array
     * @param <T> the component type of the array
     */
    public static <T> @NotNull @UnmodifiableView List<T> arrayView(T @NotNull [] array) {
        Objects.requireNonNull(array);
        if (array.length == 0) {
            return List.of();
        }

        return new ViewList<>(array);
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
     * Works the same as {@link Containers#mappedView(Function, Collection)}, but uses an array instead of a collection.
     *
     * @param mapper the mapper function
     * @param original the original array
     * @return a mapped view of the array
     * @param <T> the upper bounds of the original collection
     * @param <R> the component type of the mapped collection
     */
    public static <T, R> @NotNull List<R> mappedView(@NotNull Function<? super T, ? extends R> mapper,
            T @NotNull [] original) {
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

    private static final class ViewList<T> extends AbstractList<T> implements RandomAccess {
        private final T[] array;

        private ViewList(T[] array) {
            this.array = array;
        }

        @Override
        public T get(int index) {
            return array[index];
        }

        @Override
        public int size() {
            return array.length;
        }

        @Override
        public Stream<T> stream() {
            return Arrays.stream(array);
        }

        @Override
        public Stream<T> parallelStream() {
            return Arrays.stream(array).parallel();
        }

        @NotNull
        @Override
        public Object @NotNull [] toArray() {
            Object[] copy = new Object[array.length];
            System.arraycopy(array, 0, copy, 0, array.length);
            return copy;
        }

        @Override
        public @NotNull Iterator<T> iterator() {
            return new Iterators.ArrayIterator<>(array, 0);
        }

        @Override
        public @NotNull ListIterator<T> listIterator() {
            return new Iterators.ArrayListIterator<>(array, 0);
        }

        @Override
        public @NotNull ListIterator<T> listIterator(int index) {
            Objects.checkIndex(index, array.length + 1);
            return new Iterators.ArrayListIterator<>(array, index);
        }

        @SuppressWarnings({"unchecked", "SuspiciousSystemArraycopy"})
        @Override
        public <T1> T1 @NotNull [] toArray(T1 [] a) {
            if (a.length < array.length) {
                T1[] newArray = (T1[]) Array.newInstance(a.getClass().getComponentType(), array.length);
                System.arraycopy(array, 0, newArray, 0, array.length);
                return newArray;
            }

            System.arraycopy(array, 0, a, 0, array.length);
            if (a.length > array.length) {
                a[array.length] = null;
            }

            return a;
        }
    }
}
