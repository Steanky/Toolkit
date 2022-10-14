package com.github.steanky.toolkit.function;

/**
 * Basic implementation of {@link Wrapper}.
 *
 * @param <T> the type of object held in this wrapper
 */
class BasicWrapper<T> implements Wrapper<T> {
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
    public void set(T newValue) {
        value = newValue;
    }

    @Override
    public T get() {
        return value;
    }
}
