package com.orbidroid.orbidroid_backend.helper.misc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Any change in the original class should be reflected in this class.
class BijectionTest {

    // TOKEN related
    @Test
    void admin() {
        assertTrue(Bijection.adminTokenCorrect(Bijection.getAdminToken()));
    }

    @Test
    void getDefaultPwdForDoc() {
        assertEquals("123456", Bijection.getDefaultPwdForDoc());
    }

    // TIME related
    @Test
    void getGeneral() {
        assertEquals("08:00:00", Bijection.getGeneralWorkStartTime());
        assertEquals("18:00:00", Bijection.getGeneralWorkEndTime());
    }


    // Default means not public holidays.
    @Test
    void getDefaultWorkTime() {
        assertEquals("08:30:00", Bijection.getDefaultWorkStartForMorning());
        assertEquals("2020-01-01 08:30:00", Bijection.getDefaultWorkStartForMorning("2020-01-01"));
        assertEquals("12:30:00", Bijection.getDefaultWorkEndForMorning());
        assertEquals("2020-01-01 12:30:00", Bijection.getDefaultWorkEndForMorning("2020-01-01"));
        assertEquals("13:30:00", Bijection.getDefaultWorkStartForAfternoon());
        assertEquals("2020-01-01 13:30:00", Bijection.getDefaultWorkStartForAfternoon("2020-01-01"));

        assertTrue(Bijection.isDefaultWeekEnd(1));
        assertTrue(Bijection.isDefaultWeekEnd(7));
        assertFalse(Bijection.isDefaultWeekEnd(5));
        assertFalse(Bijection.isDefaultWeekEnd(999));
        assertFalse(Bijection.isDefaultWeekEnd(-1));

        assertEquals("18:00:00", Bijection.getDefaultWorkEndForAfternoon(2));
        assertEquals("18:00:00", Bijection.getDefaultWorkEndForAfternoon(3));
        assertEquals("18:00:00", Bijection.getDefaultWorkEndForAfternoon(4));
        assertEquals("17:00:00", Bijection.getDefaultWorkEndForAfternoon(5));
        assertEquals("17:30:00", Bijection.getDefaultWorkEndForAfternoon(6));
        assertEquals("00:00:00", Bijection.getDefaultWorkEndForAfternoon(1));
        assertEquals("00:00:00", Bijection.getDefaultWorkEndForAfternoon(7));
        assertEquals("00:00:00", Bijection.getDefaultWorkEndForAfternoon(999));
        assertEquals("00:00:00", Bijection.getDefaultWorkEndForAfternoon(-1));

        assertEquals("2020-01-01 18:00:00", Bijection.getDefaultWorkEndForAfternoon("2020-01-01", 2));
        assertEquals("2020-01-01 18:00:00", Bijection.getDefaultWorkEndForAfternoon("2020-01-01", 3));
        assertEquals("2020-01-01 18:00:00", Bijection.getDefaultWorkEndForAfternoon("2020-01-01", 4));
        assertEquals("2020-01-01 17:00:00", Bijection.getDefaultWorkEndForAfternoon("2020-01-01", 5));
        assertEquals("2020-01-01 17:30:00", Bijection.getDefaultWorkEndForAfternoon("2020-01-01", 6));
        assertEquals("2020-01-01 00:00:00", Bijection.getDefaultWorkEndForAfternoon("2020-01-01", 1));
        assertEquals("2020-01-01 00:00:00", Bijection.getDefaultWorkEndForAfternoon("2020-01-01", 7));
        assertEquals("2020-01-01 00:00:00", Bijection.getDefaultWorkEndForAfternoon("2020-01-01", 999));
        assertEquals("2020-01-01 00:00:00", Bijection.getDefaultWorkEndForAfternoon("2020-01-01", -1));
    }

    @Test
    void singleNumberForMorningAndAfternoon() {
        assertEquals(12, Bijection.getMorningEndHour());
        assertEquals(30, Bijection.getMorningEndMinute());
        assertEquals(13, Bijection.getAfternoonStartHour());
        assertEquals(30, Bijection.getAfternoonStartMinute());
    }

    @Test
    void getDayEarliestAndLatest() {
        assertEquals("00:00:00", Bijection.getDayEarliest());
        assertEquals("23:59:59", Bijection.getDayLatest());
    }

    // BOOkING TYPE related
    @Test
    void getBookingType() {
        assertEquals("Type Admin", Bijection.getBookingType(0));
        assertEquals("Type 1", Bijection.getBookingType(1));
        assertEquals("Type 2", Bijection.getBookingType(2));
        assertEquals("Type 3", Bijection.getBookingType(3));
        assertEquals("", Bijection.getBookingType(999));
        assertEquals("", Bijection.getBookingType(-100));
    }

    @Test
    void isBookingTypeCorrect() {
        assertTrue(Bijection.isBookingTypeCorrect(0));
        assertTrue(Bijection.isBookingTypeCorrect(1));
        assertTrue(Bijection.isBookingTypeCorrect(2));
        assertTrue(Bijection.isBookingTypeCorrect(3));
        assertFalse(Bijection.isBookingTypeCorrect(999));
        assertFalse(Bijection.isBookingTypeCorrect(-100));
    }

    // DURATION related
    @Test
    void getDuration() {
        assertEquals(20, Bijection.getDuration(0));
        assertEquals(20, Bijection.getDuration(1));
        assertEquals(40, Bijection.getDuration(2));
        assertEquals(40, Bijection.getDuration(3));
        assertEquals(0, Bijection.getDuration(999));
        assertEquals(0, Bijection.getDuration(-100));
    }

    @Test
    void getShortestDuration() {
        assertEquals(20, Bijection.getShortestDurationForBooking());
    }

    @Test
    void getShortestScheduleDuration() {
        assertEquals(30, Bijection.getShortestScheduleDuration());
    }

    // FORMAT CHECK related
    @Test
    void tableExist() {
        assertTrue(Bijection.tableExist("Doctors"));
        assertTrue(Bijection.tableExist("Students"));
        assertTrue(Bijection.tableExist("Bookings"));
        assertTrue(Bijection.tableExist("Schedules"));
        assertFalse(Bijection.tableExist("Hello World!"));
    }

    @Test
    void isGenderCorrect() {
        assertTrue(Bijection.isGenderCorrect("Male"));
        assertTrue(Bijection.isGenderCorrect("Female"));
        assertFalse(Bijection.isGenderCorrect("Hello world!"));
    }

    @Test
    void isPosCorrect() {
        assertTrue(Bijection.isPosCorrect("Type A"));
        assertTrue(Bijection.isPosCorrect("Type a"));
        assertTrue(Bijection.isPosCorrect("Type B"));
        assertTrue(Bijection.isPosCorrect("Type b"));
        assertTrue(Bijection.isPosCorrect("Type C"));
        assertTrue(Bijection.isPosCorrect("Type c"));
        assertTrue(Bijection.isPosCorrect("Type D"));
        assertTrue(Bijection.isPosCorrect("Type d"));
        assertFalse(Bijection.isPosCorrect("Hello world!"));
    }

    // EMAIL related
    @Test
    void getVeriCodeTimeOutInMinutes() {
        assertEquals(5, Bijection.getVeriCodeTimeOutInMinutes());
    }

    @Test
    void checkEmailInformation() {
        assertEquals("857128855@qq.com", Bijection.getEmailSenderAccount());
        assertEquals("ontkrenjxqskbfab", Bijection.getEmailSenderPwd());
        assertEquals("smtp.qq.com", Bijection.getSmtpHostAddress());
    }
}