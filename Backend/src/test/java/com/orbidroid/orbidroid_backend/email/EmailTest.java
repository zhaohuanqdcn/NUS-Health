package com.orbidroid.orbidroid_backend.email;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    @Test
    void isNusDomain() {
        assertTrue(Email.isNusDomain("E0000000@u.nus.edu"));
        assertTrue(Email.isNusDomain("ZHANGSAN@u.nus.edu"));
        assertTrue(Email.isNusDomain("E0000000@nus.edu.sg"));
        assertTrue(Email.isNusDomain("HAHA@u.yale-nus.edu.sg"));
        assertTrue(Email.isNusDomain("E12345@u.duke.nus.edu"));
        assertFalse(Email.isNusDomain("E0000000@uha.nus.edu"));
        assertFalse(Email.isNusDomain("E00000"));
    }
}