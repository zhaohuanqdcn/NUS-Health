package com.orbidroid.orbidroid_backend.helper.auth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordTest {

    @Test
    void encrypt() {
        assertEquals("e10adc3949ba59abbe56e057f20f883e", Password.encrypt("123456"));
    }

    @Test
    void encryptException() {
        Exception e1 = assertThrows(
          IllegalArgumentException.class,
                () -> Password.encrypt(null)
        );

        Exception e2 = assertThrows(
                IllegalArgumentException.class,
                () -> Password.encrypt("")
        );

        assertTrue(e1.getMessage().contains("String to encript cannot be null or zero length."));
        assertTrue(e2.getMessage().contains("String to encript cannot be null or zero length."));
    }
}