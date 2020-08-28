package com.orbidroid.orbidroid_backend.helper.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Adder {

    // Currently, a change in hours may or may not change the date (day/month/year).
    // If the method cannot add the duration to the original datetime, the original datetime is returned.
    public static String add(String dateTime, int duration) {
        if (!Parser.formatCheck(dateTime)) {
            return dateTime;
        } else {
            int min = Parser.getMin(dateTime);
            int afterMin = duration + min;
            int hour = Parser.getHour(dateTime);
            int sec = Parser.getSec(dateTime);
            while (afterMin >= 60) {
                afterMin -= 60;
                hour++;
            }
            String date = "";
            while (hour >= 24) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date d = new Date(simpleDateFormat.parse(Cutter.getDate(dateTime)).getTime()+24*3600*1000);
                    date = simpleDateFormat.format(d);
                } catch (ParseException e) {
                }
                hour -= 24;
            }
            if (date.equals("")) {
                return String.format("%s %02d:%02d:%02d", Cutter.getDate(dateTime), hour, afterMin, sec);
            } else {
                return String.format("%s %02d:%02d:%02d", date, hour, afterMin, sec);
            }
        }
    }
}
