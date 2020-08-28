package com.orbidroid.orbidroid_backend.helper.time;

import com.orbidroid.orbidroid_backend.helper.misc.Bijection;

// There should not be a try block (exception handling) inside methods of this class,
// since the methods inside it are rather fundamental.
// Format checking should be done prior to the calling of methods in this class.

public class Comparator {

    // return true if 1's date is earlier than 2's date
    public static boolean isDateEarlier(String dateTime1, String dateTime2) {
        if (Parser.getYear(dateTime1) < Parser.getYear(dateTime2)) {
            return true;
        } else if (Parser.getYear(dateTime1) > Parser.getYear(dateTime2)) {
            return false;
        }

        if (Parser.getMonth(dateTime1) < Parser.getMonth(dateTime2)) {
            return true;
        } else if (Parser.getMonth(dateTime1) > Parser.getMonth(dateTime2)) {
            return false;
        }

        if (Parser.getDay(dateTime1) < Parser.getDay(dateTime2)) {
            return true;
        } else if (Parser.getDay(dateTime1) > Parser.getDay(dateTime2)) {
            return false;
        }

        return false;
    }
     public static boolean isDateSame(String dateTime1, String dateTime2) {
        return Parser.getYear(dateTime1) == Parser.getYear(dateTime2)
                && Parser.getMonth(dateTime1) == Parser.getMonth(dateTime2)
                && Parser.getDay(dateTime1) == Parser.getDay(dateTime2);
    }

    public static boolean isDateTimeDateSameWithDate(String dateTime, String date) {
        return Parser.getYear(dateTime) == Parser.getYear(date)
                && Parser.getMonth(dateTime) == Parser.getMonth(date)
                && Parser.getDay(dateTime) == Parser.getDay(date);
    }

    // we assume the given dateTime 1 and 2 is the same in terms of date
    // we only consider hour and minute in this case, because seconds are trivial
    public static boolean isTimeEalier(String dateTime1, String dateTime2) {
        if (Parser.getHour(dateTime1) < Parser.getHour(dateTime2)) {
            return true;
        } else if (Parser.getHour(dateTime1) > Parser.getHour(dateTime2)) {
            return false;
        }

        if (Parser.getMin(dateTime1) < Parser.getMin(dateTime2)) {
            return true;
        } else if (Parser.getMin(dateTime1) > Parser.getMin(dateTime2)) {
            return false;
        }
        return false;
    }

    public static boolean isTimeSame(String dateTime1, String dateTime2) {
        return Parser.getHour(dateTime1) == Parser.getHour(dateTime2)
                && Parser.getMin(dateTime1) == Parser.getMin(dateTime2);
    }

    public static boolean isDateTimeSame(String dateTime1, String dateTime2) {
        return isDateSame(dateTime1, dateTime2) && isTimeSame(dateTime1, dateTime2);
    }

    public static boolean isDateTimeEarlier(String dateTime1, String dateTime2) {
        if (isDateEarlier(dateTime1, dateTime2)) {
            return true;
        } else if (isDateEarlier(dateTime2, dateTime1)) {
            return false;
        }
        if (isTimeEalier(dateTime1, dateTime2)) {
            return true;
        } else if (isTimeEalier(dateTime2, dateTime1)) {
            return false;
        }
        return false;
    }

    // Since this function is rather fundamental,
    // input argument format is assumed to be correct and no additional checking is needed.
    public static boolean hasConflict(String start1, String end1, String start2, String end2) {
        return (isDateTimeEarlier(start2, end1) && isDateTimeEarlier(start1, start2))
                || (isDateTimeEarlier(start2, start1) && isDateTimeEarlier(end1, end2))
                || (isDateTimeEarlier(start2, start1) && isDateTimeEarlier(start1, end2))
                || (isDateTimeEarlier(start1, end2) && isDateTimeEarlier(start2, start1))
                || (isDateTimeEarlier(start1, start2) && isDateTimeEarlier(end2, end1))
                || (isDateTimeEarlier(start1, start2) && isDateTimeEarlier(start2, end1))
                || (isDateTimeSame(start1, start2))
                || (isDateTimeSame(end1, end2));
    }

    public static boolean isMorning(String dateTime) {
        int hour = Parser.getHour(dateTime);
        int min = Parser.getMin(dateTime);
        return hour < Bijection.getMorningEndHour()
                ||
                (hour == Bijection.getMorningEndHour() && min <= Bijection.getMorningEndMinute());
    }

    public static boolean isAfternoon(String dateTime) {
        int hour = Parser.getHour(dateTime);
        int min = Parser.getMin(dateTime);
        return hour > Bijection.getAfternoonStartHour()
                || (
                  hour == Bijection.getAfternoonStartHour() && min >= Bijection.getAfternoonStartMinute()
                );
    }
}
