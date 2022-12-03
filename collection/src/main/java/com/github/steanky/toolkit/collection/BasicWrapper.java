package com.github.steanky.toolkit.collection;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.stream.Stream;

/**
 * Basic implementation of {@link Wrapper}.
 *
 * @param <T> the type of object held in this wrapper
 */
class BasicWrapper<T> extends AbstractList<T> implements Wrapper<T>, RandomAccess {
    private T value;

    /**
     * Creates a new instance of this class with the provided initial wrapper.
     *
     * @param initialValue the initial value for this wrapper
     */
    BasicWrapper(T initialValue) {
        this.value = initialValue;
    }

    @Override
    public T set(T newValue) {
        return value = newValue;
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(get());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        if (obj instanceof Wrapper<?> other) {
            return Objects.equals(get(), other.get());
        }

        return false;
    }

    @Override
    public String toString() {
        return "BasicWrapper{value=" + get() + "}";
    }

    @Override
    public T get(int index) {
        Objects.checkIndex(index, 1);
        return value;
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public Stream<T> stream() {
        return Stream.of(value);
    }

    @Override
    public Stream<T> parallelStream() {
        return stream();
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterators.SingletonIterator<>(value);
    }

    @Override
    public ListIterator<T> listIterator() {
        return new Iterators.SingletonListIterator<>(value);
    }

    @Override
    public int indexOf(Object o) {
        return index(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return index(o);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return Objects.equals(o, value);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        for (Object object : c) {
            if (!Objects.equals(object, value)) {
                return false;
            }
        }

        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T1> T1[] toArray(IntFunction<T1[]> generator) {
        Object[] array = generator.apply(1);

        //may generate ArrayStoreException as per the specification
        array[0] = value;
        return (T1[]) array;
    }

    @Override
    public Object @NotNull [] toArray() {
        Object[] array = new Object[1];
        array[0] = value;
        return array;
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        action.accept(value);
    }

    //shared logic for indexOf, lastIndexOf
    private int index(Object o) {
        return Objects.equals(o, value) ? 0 : -1;
    }
}
