package com.orbidroid.orbidroid_backend.helper.misc;

import com.orbidroid.orbidroid_backend.helper.auth.Password;

// This class serves the same purpose as the application properties file.
// This class contains all miscellaneous data of the project.
// All data inside this class should be carefully documented.

// Since this class is fundamental, no parameter input checking is done.
// Proper checking should be done prior to the calling of methods in this class.

// Any change in this class should be reflected in the relevant test class.
public class Bijection {

    // TOKEN related
    public static String getAdminToken() {
        return "123456mm";
    }

    public static boolean adminTokenCorrect(String token) {
        return token.equals("123456mm");
    }

    public static String getDefaultPwdForDoc() {
        return "123456";
    }

    // TIME related

    @Deprecated
    public static String getGeneralWorkStartTime() {
        return "08:00:00";
    }

    @Deprecated
    public static String getGeneralWorkEndTime() {
        return "18:00:00";
    }

    public static String getDayEarliest() {
        return "00:00:00";
    }

    public static String getDayLatest() {
        return "23:59:59";
    }

    public static int getMorningEndHour() {
        return 12;
    }

    public static int getMorningEndMinute() {
        return 30;
    }

    public static int getAfternoonStartHour() {
        return 13;
    }

    public static int getAfternoonStartMinute() {
        return 30;
    }

    public static int getShortestScheduleDuration() {
        return 30;
    }

    // For the following 2 functions,
    // the input argument cannot be 1 or 7, as specified in the isDefaultWeekEnd function below.
    // This checking should be done manually before the function is called.

    // default means not public holidays
    public static String getDefaultWorkStartForMorning() {
        return "08:30:00";
    }

    public static String getDefaultWorkStartForMorning(String date) {
        return date + " 08:30:00";
    }

    // default means not public holidays
    public static String getDefaultWorkEndForMorning() {
        return "12:30:00";
    }

    public static String getDefaultWorkEndForMorning(String date) {
        return date + " 12:30:00";
    }

    // default means not public holidays
    public static String getDefaultWorkStartForAfternoon() {
        return "13:30:00";
    }

    public static String getDefaultWorkStartForAfternoon(String date) {
        return date + " 13:30:00";
    }

    // default means not public holidays
    public static String getDefaultWorkEndForAfternoon(int weekday) {
        if (weekday <= 4 && weekday >= 2) {
            return "18:00:00";
        }
        if (weekday == 5) {
            return "17:00:00";
        }
        if (weekday == 6) {
            return "17:30:00";
        }

        // this is an invalid time value; if proper checking is done before this function is called,
        // this value will never be returned;
        // however, if this value is returned, it won't result in serious errors, since
        // the time is 00:00:00, and no slot before this time can be inserted.
        return "00:00:00";
    }

    public static String getDefaultWorkEndForAfternoon(String date, int weekday) {
        if (weekday <= 4 && weekday >= 2) {
            return date + " 18:00:00";
        }
        if (weekday == 5) {
            return date + " 17:00:00";
        }
        if (weekday == 6) {
            return date + " 17:30:00";
        }
        return date + " 00:00:00";
    }

    // The function below will only be called with argument from
    // the mapper function, therefore no input checking is needed.
    // (The input will be limited to 1, 2, ..., 7)
    // default means not public holidays
    public static boolean isDefaultWeekEnd(int dayOfWeek) {
        // 1 means Sunday and 7 means weekday
        return dayOfWeek == 1 || dayOfWeek == 7;
    }

    // BOOKING TYPE related
    public static String getBookingType(int bookingType) {
        if (bookingType == 0) {
            return "Type A";
        }
        if (bookingType == 1) {
            return "Type B";
        }
        if (bookingType == 2) {
            return "Type C";
        }
        if (bookingType == 3) {
            return "Type D";
        }
        return "";
    }

    public static boolean isBookingTypeCorrect(int bookingType) {
        if (bookingType == 0) {
            return true;
        }
        if (bookingType == 1) {
            return true;
        }
        if (bookingType == 2) {
            return true;
        }
        if (bookingType == 3) {
            return true;
        }
        return false;
    }

    // DURATION related
    public static int getDuration(int bookingType) {
        if (bookingType == 0) {
            return 20;
        }
        if (bookingType == 1) {
            return 20;
        }
        if (bookingType == 2) {
            return 40;
        }
        if (bookingType == 3) {
            return 40;
        }
        return 0;
    }

    public static int getShortestDurationForBooking() {
        return 20;
    }

    // FORMAT CHECK related
    public static boolean tableExist(String tableName) {
        if (tableName.equals("Doctors")) {
            return true;
        }
        if (tableName.equals("Students")) {
            return true;
        }
        if (tableName.equals("Bookings")) {
            return true;
        }
        if (tableName.equals("Schedules")) {
            return true;
        }
        return false;
    }

    public static boolean isPosCorrect(String pos) {
        char type = pos.toLowerCase().charAt(5);

        if (type == 'a' || type == 'b'|| type == 'c'|| type == 'd') {
            return true;
        }
        return false;
    }

    public static boolean isGenderCorrect(String gender) {
        if (gender.equals("Male")) {
            return true;
        }
        if (gender.equals("Female")) {
            return true;
        }
        return false;
    }

    // EMAIL related
    public static int getVeriCodeTimeOutInMinutes() {
        return 5;
    }

    public static String getEmailSenderAccount() {
        return "857128855@qq.com";
    }

    public static String getEmailSenderPwd() {
        return "ontkrenjxqskbfab";
    }

    public static String getSmtpHostAddress() {
        return "smtp.qq.com";
    }
}
