package orbroid.nushealth.nushealthapp.entity;

import android.icu.text.UFormat;
import android.icu.util.Calendar;

import java.text.Normalizer;

import orbroid.nushealth.nushealthapp.formatting.BookingTime;
import orbroid.nushealth.nushealthapp.utility.FormatManager;

public class NewBooking {
    public static String stuId;
    public static Doctor doctor;
    public static Calendar date;
    public static BookingTime time;
    public static int type;
    public static int bookingId = -1;

    public static String getType() {
        return "Type " + (char)(NewBooking.type + 'A')
                + String.format(" (%d min)", (type <= 1 ? 20 : 40));
    }

    public static String getDoctor() {
        return doctor.getName();
    }

    public static String getDate() {
        final int mYear = date.get(Calendar.YEAR);
        final int mMonth = date.get(Calendar.MONTH);
        final int mDay = date.get(Calendar.DAY_OF_MONTH);
        return FormatManager.format(mYear, mMonth+1, mDay);
    }

    public static String getTime() {
        return time.getTime();
    }

    public static String getBookingDetail() {
        String detail = bookingId == -1 ? "" : "Number: " + bookingId + "\n";
        detail += "Name: " + Student.getName()
                + "\nType: " + NewBooking.getType()
                + "\nDate: " + NewBooking.getDate()
                + "\nTime: " + NewBooking.getTime()
                + "\nDoctor: " + NewBooking.getDoctor();
        return detail;
    }

}
