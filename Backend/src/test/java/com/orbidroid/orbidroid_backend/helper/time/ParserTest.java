package com.orbidroid.orbidroid_backend.helper.time;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    private static final String test1 = "2020-06-07 14:39:28";
    private static final String test2 = "2020-11-29 09:03:08";
    private static final String test3 = "2020-13-29 09:03:08";
    private static final String test4 = "2020-12-32 09:03:08";
    private static final String test5 = "1900-12-29 09:03:08";
    private static final String test6 = "2020-12-29 27:03:08";
    private static final String test7 = "2020-12-29 09:99:08";
    private static final String test8 = "1900-12-29 09:03:78";
    private static final String test9 = "2020-06-07 14:39:280";
    private static final String test10 = "2020-06-0 14:39:28";
    private static final String test11 = "2020-06-0w 14:39:28";
    private static final String test12 = "2020-06-07gg4:39:28";

    private static final String test13 = "2020-06-07";
    private static final String test14 = "2020-06-32";
    private static final String test15 = "1999-06-07";
    private static final String test16 = "2020-82-07";
    private static final String test17 = "2020-e6-07";
    private static final String test18 = "2020-0-07";

    @Test
    void getSec() {
        assertEquals(28, Parser.getSec(test1));
    }

    @Test
    void getMin() {
        assertEquals(39, Parser.getMin(test1));
    }

    @Test
    void getHour() {
        assertEquals(14, Parser.getHour(test1));
    }

    @Test
    void getDay() {
        assertEquals(7, Parser.getDay(test1));
    }

    @Test
    void getMonth() {
        assertEquals(6, Parser.getMonth(test1));
    }

    @Test
    void getYear() {
        assertEquals(2020, Parser.getYear(test1));
    }

    @Test
    void formatCheck() {
        assertTrue(Parser.formatCheck(test1));
        assertTrue(Parser.formatCheck(test2));
        assertFalse(Parser.formatCheck(test3));
        assertFalse(Parser.formatCheck(test4));
        assertFalse(Parser.formatCheck(test5));
        assertFalse(Parser.formatCheck(test6));
        assertFalse(Parser.formatCheck(test7));
        assertFalse(Parser.formatCheck(test8));
        assertFalse(Parser.formatCheck(test9));
        assertFalse(Parser.formatCheck(test10));
        assertFalse(Parser.formatCheck(test11));
        assertFalse(Parser.formatCheck(test12));
    }

    @Test
    void formatCheckForDate() {
        assertTrue(Parser.formatCheckForDate(test13));
        assertFalse(Parser.formatCheckForDate(test14));
        assertFalse(Parser.formatCheckForDate(test15));
        assertFalse(Parser.formatCheckForDate(test16));
        assertFalse(Parser.formatCheckForDate(test17));
        assertFalse(Parser.formatCheckForDate(test18));
    }
}