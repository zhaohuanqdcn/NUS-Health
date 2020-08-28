package com.orbidroid.orbidroid_backend.helper.time;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComparatorTest {
    private static final String test1 = "2020-06-07 14:38:00";
    private static final String test2 = "2020-06-07 14:39:00";

    private static final String test3 = "2020-06-06 14:40:00";
    private static final String test4 = "2020-06-07 14:39:00";

    private static final String test5 = "2019-07-07 14:39:00";
    private static final String test6 = "2020-06-07 14:39:00";

    private static final String test7 = "2020-06-07 14:57:00";
    private static final String test8 = "2020-07-07 14:39:00";

    private static final String test9 = "2020-01-07 14:39:00";
    private static final String test10 = "2020-06-07 14:39:00";

    private static final String test11 = "2020-06-07 08:39:47";
    private static final String test12 = "2020-06-07 08:40:00";

    private static final String test13 = "2020-06-07 08:40:00";
    private static final String test14 = "2020-06-07 08:40:00";

    private static final String test15 = "2020-06-07 07:40:00";
    private static final String test16 = "2020-06-07 08:50:00";
    private static final String test17 = "2020-06-07 09:40:00";
    private static final String test18 = "2020-06-08 07:40:00";

    private static final String test19 = "2020-07-08 07:40:00";
    private static final String test20 = "2020-06-08 07:40:00";
    private static final String test21 = "2020-06-08 07:30:00";
    private static final String test22 = "2020-06-08 07:40:00";

    private static final String test23 = "2020-06-08 07:30:00";
    private static final String test24 = "2020-06-08 07:30:00";

    private static final String test25 = "2020-06-09 06:30:00";
    private static final String test26 = "2020-06-09 07:30:00";

    private static final String test27 = "2020-06-09 08:30:00";
    private static final String test28 = "2020-06-09 07:30:00";

    private static final String test29 = "2021-06-09 08:30:00";
    private static final String test30 = "2020-06-09 07:30:00";
    private static final String test31 = "2020-05-09 07:30:00";
    private static final String test32 = "2020-05-08 07:30:00";

    private static final String test33 = "2020-05-09 07:58:00";
    private static final String test34 = "2020-05-08 07:30:00";

    private static final String test35 = "2020-05-09 07:58:00";
    private static final String test36 = "2020-05-08 07:30:00";
    private static final String test37 = "2020-03-29 07:58:00";
    private static final String test38 = "2020-03-29 07:30:00";

    @Test
    void isDateEarlier() {
        assertFalse(Comparator.isDateEarlier(test29, test30));
        assertFalse(Comparator.isDateEarlier(test30, test31));
        assertFalse(Comparator.isDateEarlier(test31, test32));
    }

    @Test
    void isDateSame() {
        assertTrue(Comparator.isDateSame(test15, test16));
        assertFalse(Comparator.isDateSame(test17, test18));
    }

    @Test
    void isDateTimeDateSameWithDate() {
        assertTrue(Comparator.isDateTimeDateSameWithDate(test1, "2020-06-07"));
        assertFalse(Comparator.isDateTimeDateSameWithDate(test1, "2020-06-08"));
        assertFalse(Comparator.isDateTimeDateSameWithDate(test1, "2020-07-07"));
        assertFalse(Comparator.isDateTimeDateSameWithDate(test1, "2019-06-07"));
    }

    @Test
    void isTimeEalier() {
        assertTrue(Comparator.isTimeEalier(test25, test26));
        assertFalse(Comparator.isTimeEalier(test27, test28));
        assertFalse(Comparator.isTimeEalier(test33, test34));
    }

    @Test
    void isTimeSame() {
        assertTrue(Comparator.isTimeSame(test19, test20));
        assertFalse(Comparator.isTimeSame(test21, test22));
    }

    @Test
    void isDateTimeSame() {
        assertTrue(Comparator.isDateTimeSame(test23, test24));
    }

    @Test
    void isDateTimeEarlier() {
        assertTrue(Comparator.isDateTimeEarlier(test1, test2));
        assertTrue(Comparator.isDateTimeEarlier(test3, test4));
        assertTrue(Comparator.isDateTimeEarlier(test5, test6));
        assertTrue(Comparator.isDateTimeEarlier(test7, test8));
        assertTrue(Comparator.isDateTimeEarlier(test9, test10));
        assertTrue(Comparator.isDateTimeEarlier(test11, test12));
        assertFalse(Comparator.isDateTimeEarlier(test13, test14));
        assertFalse(Comparator.isDateTimeEarlier(test35, test36));
        assertFalse(Comparator.isDateTimeEarlier(test37, test38));
    }

    // This method is of critical importance so test cases are given separately inside this test method.
    @Test
    void hasConflict() {
        String s1 = "2020-01-01 10:00:00";
        String e1 = "2020-01-01 10:30:00";
        String s2 = "2020-01-01 10:30:00";
        String e2 = "2020-01-01 11:00:00";
        assertFalse(Comparator.hasConflict(s1, e1, s2, e2));

        String s3 = "2020-01-01 09:30:00";
        String e3 = "2020-01-01 10:00:00";
        assertFalse(Comparator.hasConflict(s1, e1, s3, e3));

        String s4 = "2020-01-01 10:10:00";
        String e4 = "2020-01-01 10:20:00";
        assertTrue(Comparator.hasConflict(s1, e1, s4, e4));

        String s5 = "2020-01-01 10:10:00";
        String e5 = "2020-01-01 10:30:00";
        assertTrue(Comparator.hasConflict(s1, e1, s5, e5));

        String s6 = "2020-01-01 10:00:00";
        String e6 = "2020-01-01 10:20:00";
        assertTrue(Comparator.hasConflict(s1, e1, s6, e6));

        String s7 = "2020-01-01 10:15:00";
        String e7 = "2020-01-01 10:45:00";
        assertTrue(Comparator.hasConflict(s1, e1, s7, e7));

        String s8 = "2020-01-01 09:45:00";
        String e8 = "2020-01-01 10:15:00";
        assertTrue(Comparator.hasConflict(s1, e1, s8, e8));

        String s9 = "2020-01-01 09:45:00";
        String e9 = "2020-01-01 10:45:00";
        assertTrue(Comparator.hasConflict(s1, e1, s9, e9));
    }

    @Test
    void isMorning() {
        assertFalse(Comparator.isMorning(test1));
        assertFalse(Comparator.isMorning(test2));
        assertFalse(Comparator.isMorning(test3));
        assertTrue(Comparator.isMorning(test36));
        assertTrue(Comparator.isMorning(test37));
        assertTrue(Comparator.isMorning(test38));
    }

    @Test
    void isAfternoon() {
        assertTrue(Comparator.isAfternoon(test1));
        assertTrue(Comparator.isAfternoon(test2));
        assertTrue(Comparator.isAfternoon(test3));
        assertFalse(Comparator.isAfternoon(test36));
        assertFalse(Comparator.isAfternoon(test37));
        assertFalse(Comparator.isAfternoon(test38));
    }
}