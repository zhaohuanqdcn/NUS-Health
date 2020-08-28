package com.orbidroid.orbidroid_backend.helper.time;

public class Cutter {
    public static String getDate(String dateTime) {
        return dateTime.substring(0, 10);
    }
    public static String getTime(String dateTime) {
        return dateTime.substring(11, 19);
    }
    public static String trimTimeFromDb(String dateTime) {
        return dateTime.substring(0, 19);
    }
}
