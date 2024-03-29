package com.github.steanky.toolkit.collection;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Basic implementation of {@link Wrapper}.
 *
 * @param <T> the type of object held in this wrapper
 */
final class WrapperImpl<T> extends AbstractList<T> implements Wrapper<T>, RandomAccess {
    private T value;

    /**
     * Creates a new instance of this class with the provided initial wrapper.
     *
     * @param initialValue the initial value for this wrapper
     */
    WrapperImpl(T initialValue) {
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
        T value = this.value;
        return 31 + (value == null ? 0 : value.hashCode());
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
            return Objects.equals(value, other.get());
        }
        else if (obj instanceof List<?> other) {
            Iterator<?> itr = other.iterator();

            if (!itr.hasNext()) { //empty list, not equal to us
                return false;
            }

            Object first = itr.next();
            if (itr.hasNext()) { //list has more than one element, not equal to us
                return false;
            }

            return Objects.equals(value, first); //list reported only one element, compare for equality
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
    public T set(int index, T element) {
        Objects.checkIndex(index, 1);
        return value = element;
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

    @Override
    public Object @NotNull [] toArray() {
        return new Object[] {
                value
        };
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T1> T1 @NotNull [] toArray(@NotNull T1 @NotNull [] array) {
        if (array.length == 0) {
            Object[] newArray = (Object[]) Array.newInstance(array.getClass().getComponentType(), 1);
            newArray[0] = value;
            return (T1[]) newArray;
        }

        ((Object[]) array)[0] = value;
        if (array.length > 1) {
            ((Object[]) array)[1] = null;
        }

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
