package com.github.steanky.toolkit.collection;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BasicWrapperTest {
    @Test
    void equalsHashCodeCompatibleWithList() {
        String s = "test";

        Wrapper<String> wrapper = Wrapper.of(s);
        List<String> list = List.of(s);

        assertEquals(list, wrapper);
        assertEquals(list.hashCode(), wrapper.hashCode());
    }
}