package com.orbidroid.orbidroid_backend.helper.time;

import java.text.ParseException;

public class Parser {
    public static int getSec(String dateTime) {
        return Integer.valueOf(dateTime.substring(17, 19));
    }

    public static int getMin(String dateTime) {
        return Integer.valueOf(dateTime.substring(14, 16));
    }

    public static int getHour(String dateTime) {
        return Integer.valueOf(dateTime.substring(11, 13));
    }

    public static int getDay(String dateTime) {
        return Integer.valueOf(dateTime.substring(8, 10));
    }

    public static int getMonth(String dateTime) {
        return Integer.valueOf(dateTime.substring(5, 7));
    }

    public static int getYear(String dateTime) {
        return Integer.valueOf(dateTime.substring(0, 4));
    }

    // no checking is needed for seconds
    public static boolean formatCheck(String dateTime) throws IllegalArgumentException {
        try {
            if (dateTime.length() != 19) {
                return false;
            }
            // currently no need
            /*
            if (!(dateTime.charAt(4) == '-'
                    && dateTime.charAt(7) == '-'
                    && dateTime.charAt(10) == ' '
                    && dateTime.charAt(13) == ':'
                    && dateTime.charAt(16) == ':'
            )) {
                return false;
            }
            */
                int year = Integer.parseInt(dateTime.substring(0, 4));
                int month = Integer.parseInt(dateTime.substring(5, 7));
                int day = Integer.parseInt(dateTime.substring(8, 10));
                int hour = Integer.parseInt(dateTime.substring(11, 13));
                int min = Integer.parseInt(dateTime.substring(14, 16));
                int sec = Integer.parseInt(dateTime.substring(17, 19));
                return year > 2000
                        && (month >= 1 && month <= 12)
                        && (day <= 31 && day >= 1)
                        && (hour < 24 && hour >= 0)
                        && (min < 60 && min >= 0)
                        && (sec < 60 && sec >= 0)
                        && dateTime.charAt(4) == '-'
                        && dateTime.charAt(7) == '-'
                        && dateTime.charAt(13) == ':'
                        && dateTime.charAt(16) == ':';
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean formatCheckForDate(String date) {
        try {
            if (date.length() != 10) {
                return false;
            }
            // currently no need
            /*
            if (!(date.charAt(4) == '-'
                    && date.charAt(7) == '-')) {
                return false;
            }
            */
            int year = Integer.parseInt(date.substring(0, 4));
            int month = Integer.parseInt(date.substring(5, 7));
            int day = Integer.parseInt(date.substring(8, 10));
            return year > 2000
                    && (month >= 1 && month <= 12)
                    && (day <= 31 && day >= 1);
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
