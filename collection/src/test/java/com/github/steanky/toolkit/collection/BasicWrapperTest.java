package com.github.steanky.toolkit.collection;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BasicWrapperTest {
    @SuppressWarnings("SimplifiableAssertion")
    @Test
    void equalsHashCodeCompatibleWithList() {
        String s = "test";

        Wrapper<String> wrapper = Wrapper.of(s);
        List<String> list = List.of(s);

        List<String> unequalList = List.of(s, s);

        assertTrue(wrapper.equals(list), "wrapper does not equal list");
        assertTrue(list.equals(wrapper), "list does not equal wrapper");

        assertEquals(list.hashCode(), wrapper.hashCode(), "list hashcode and wrapper hashcode do not match");

        assertFalse(wrapper.equals(unequalList), "wrapper equals unequal list");
        assertFalse(unequalList.equals(wrapper), "unequal list equals wrapper");
    }
}