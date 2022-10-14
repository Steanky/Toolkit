package com.github.steanky.toolkit.function;

import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class MemoizingSupplierTest {
    private static class TestObject {}

    private static class Counter {
        int i;
    }

    @Test
    void objectIdentity() {
        Counter counter = new Counter();
        Supplier<TestObject> objectSupplier = () -> {
            counter.i++;
            return new TestObject();
        };
        MemoizingSupplier<TestObject> memoizingSupplier = new MemoizingSupplier<>(objectSupplier);

        TestObject firstMemoizedCall = memoizingSupplier.get();
        assertEquals(1, counter.i);

        assertNotNull(firstMemoizedCall);
        assertNotSame(objectSupplier.get(), firstMemoizedCall);

        for (int i = 0; i < 100; i++) {
            assertSame(firstMemoizedCall, memoizingSupplier.get());
            assertEquals(2, counter.i);
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    void nullSupplierThrows() {
        assertThrows(NullPointerException.class, () -> new MemoizingSupplier<>(null));
    }

    @Test
    void nullSuppliedValue() {
        Counter counter = new Counter();
        Supplier<Object> nullSupplier = () -> {
            counter.i++;
            return null;
        };

        MemoizingSupplier<Object> memoizingSupplier = new MemoizingSupplier<>(nullSupplier);
        assertNull(memoizingSupplier.get());

        for (int i = 0; i < 100; i++) {
            assertEquals(1, counter.i);
        }
    }
}