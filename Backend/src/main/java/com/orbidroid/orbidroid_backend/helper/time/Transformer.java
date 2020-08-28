package com.orbidroid.orbidroid_backend.helper.time;

import com.orbidroid.orbidroid_backend.helper.misc.Bijection;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Transformer {

    // Here Sunday is mapped to 1, Saturday is mapped to 7
    public static int weekdayMapper(String date) {
        try {
            Date actualDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            Calendar c = Calendar.getInstance();
            c.setTime(actualDate);
            return c.get(Calendar.DAY_OF_WEEK);
        } catch (ParseException e) {
            // Monday should be the default week day
            return 2;
        }
    }

    public static String turnIntoDayEarliest(String date) {
        return date + " " + Bijection.getDayEarliest();
    }

    public static String turnIntoDayLatest(String date) {
        return date + " " + Bijection.getDayLatest();
    }
}
