package com.orbidroid.orbidroid_backend.helper.time;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransformerTest {
    private static final String test1 = "2020-01-06";
    private static final String test2 = "2020-01-07";
    private static final String test3 = "2020-01-08";
    private static final String test4 = "2020-01-09";
    private static final String test5 = "2020-01-10";
    private static final String test6 = "2020-01-11";
    private static final String test7 = "2020-01-12";

    private static final String test8 = "202w-06-3e";
    private static final String test9 = "1999-06-070";
    private static final String test10 = "2020--11e2-07";
    private static final String test11 = "2020-e6-07";
    private static final String test12 = "2020-haha-07";

    @Test
    void weekdayMapper() {
        assertEquals(2, Transformer.weekdayMapper(test1));
        assertEquals(3, Transformer.weekdayMapper(test2));
        assertEquals(4, Transformer.weekdayMapper(test3));
        assertEquals(5, Transformer.weekdayMapper(test4));
        assertEquals(6, Transformer.weekdayMapper(test5));
        assertEquals(7, Transformer.weekdayMapper(test6));
        assertEquals(1, Transformer.weekdayMapper(test7));

        assertEquals(2, Transformer.weekdayMapper(test8));
        assertEquals(2, Transformer.weekdayMapper(test9));
        assertEquals(2, Transformer.weekdayMapper(test10));
        assertEquals(2, Transformer.weekdayMapper(test11));
        assertEquals(2, Transformer.weekdayMapper(test12));
    }

    @Test
    void turnIntoDayEarliest() {
        assertEquals(test1 + " 00:00:00", Transformer.turnIntoDayEarliest(test1));
    }

    @Test
    void turnIntoDayLatest() {
        assertEquals(test1 + " 23:59:59", Transformer.turnIntoDayLatest(test1));
    }
}