package com.github.steanky.toolkit.function;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * A thread-safe version of {@link MemoizingSupplier}. Even in cases of concurrent access, the underlying supplier is
 * guaranteed to only be called once.
 *
 * @param <T> the type of object returned by the supplier
 */
public final class ConcurrentMemozingSupplier<T> implements Supplier<T> {
    private final Supplier<? extends T> supplier;

    private volatile T cache;
    private volatile boolean cached;

    private final Object sync = new Object();

    /**
     * Creates a new instance of this class, with the specified supplier as its delegate. This supplier will only be
     * called once, even if {@link Supplier#get()} is called concurrently.
     *
     * @param supplier the delegate supplier (must be non-null)
     */
    public ConcurrentMemozingSupplier(@NotNull Supplier<? extends T> supplier) {
        this.supplier = Objects.requireNonNull(supplier);
    }

    @Override
    public T get() {
        if (cached) {
            return cache;
        }

        synchronized (sync) {
            if (cached) {
                return cache;
            }

            cache = supplier.get();
            cached = true;
            return cache;
        }
    }
}
