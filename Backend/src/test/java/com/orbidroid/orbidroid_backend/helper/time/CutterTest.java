package com.orbidroid.orbidroid_backend.helper.time;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CutterTest {
    private static final String test1 = "2020-06-07 14:39:28";
    private static final String test2 = "2020-12-29 09:03:08";
    private static final String test3 = "2020-12-29 09:03:08.0001";

    @Test
    void getDate() {
        assertEquals("2020-06-07", Cutter.getDate(test1));
        assertEquals("2020-12-29", Cutter.getDate(test2));
    }

    @Test
    void getTime() {
        assertEquals("14:39:28", Cutter.getTime(test1));
        assertEquals("09:03:08", Cutter.getTime(test2));
    }

    @Test
    void trimTimeFromDb() {
        assertEquals("2020-12-29 09:03:08", Cutter.trimTimeFromDb(test3));
    }
}