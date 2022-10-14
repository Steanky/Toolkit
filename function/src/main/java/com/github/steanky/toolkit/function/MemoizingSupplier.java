package com.github.steanky.toolkit.function;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Supplier implementation based on an underlying {@link Supplier}. When the {@link Supplier#get()} is called for the
 * first time, the underlying supplier is invoked and the result is cached. Subsequent calls to {@code get} will return
 * the same value, without querying the underlying supplier.
 * <p>
 * This class is not thread-safe. The result of calling {@code get} is undefined if it is accessed by two or more
 * threads at once.
 *
 * @param <T> the type provided by this supplier
 */
public final class MemoizingSupplier<T> implements Supplier<T> {
    private final Supplier<? extends T> supplier;

    //distinguish between null data and absent data
    private boolean hasData;
    private T cached;

    /**
     * Creates a new instance of this class.
     *
     * @param supplier the underlying supplier
     */
    public MemoizingSupplier(@NotNull Supplier<? extends T> supplier) {
        this.supplier = Objects.requireNonNull(supplier);
    }


    @Override
    public T get() {
        if (!hasData) {
            hasData = true;
            cached = supplier.get();
        }

        return cached;
    }
}
