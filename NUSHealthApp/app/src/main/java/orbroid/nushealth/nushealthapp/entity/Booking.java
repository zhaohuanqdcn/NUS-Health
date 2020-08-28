package orbroid.nushealth.nushealthapp.entity;

import android.icu.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Time;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import orbroid.nushealth.nushealthapp.formatting.BookingTime;
import orbroid.nushealth.nushealthapp.utility.Connector;
import orbroid.nushealth.nushealthapp.utility.FormatManager;
import orbroid.nushealth.nushealthapp.utility.JsonManager;

public class Booking {
    private int stuId, docId, id;
    private Time startTime;
    private String date;
    private int duration, type, hour, minute;

    public Booking(int id, String time, int dur, int docId, int stuId, int type) {
        this.id = id;
        this.startTime = FormatManager.parseTime(time);
        this.duration = dur;
        this.docId = docId;
        this.stuId = stuId;
        this.type = type;
        this.date = FormatManager.parseDate(time);
        this.hour = startTime.getHours();
        this.minute = startTime.getMinutes();
    }

    public String getType(int t) {
        return "Type " + (char)(this.type + 'A')
                + (t == 0 ? ""
                : String.format(" (%d min)", (this.type <= 1 ? 20 : 40)));
    }

    public String getDate() {
        return this.date;
    }

    public String getShortDate() {
        return getDate().substring(5);
    }

    private String getEndTime(){
        int endHour = (hour + (minute + duration) / 60) % 24;
        int endMin = (minute + duration) % 60;
        return FormatManager.format(endHour)+ ":" + FormatManager.format(endMin);
    }

    public String getTime(){
        return FormatManager.format(hour)+ ":" + FormatManager.format(minute) + " - " + getEndTime();
    }

    public int getId() {
        return this.id;
    }

    public String getBookingBrief() {
        return this.getShortDate() + "\n" + getType(0);
    }

    public String getBookingDetail() {
        return "Number:" + this.id
                + "\nName: " + Student.getName()
                + "\nType: " + getType(1)
                + "\nDate: " + getDate()
                + "\nTime: " + getTime()
                + "\nDoctor: " + docId;
    }

    public static ArrayList<Booking> getList() {
        ArrayList<Booking> list = new ArrayList<>();
        try {
            String history = CompletableFuture.supplyAsync(
                    new Supplier<String>() {
                        @Override
                        public String get() {
                            try {
                                return Connector.getBookingHistory(Student.id);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return "failed";
                        }
                    }).join();
            list = JsonManager.bookingJson(history);
            list.sort(new Comparator<Booking>() {
                @Override
                public int compare(Booking o1, Booking o2) {
                    return FormatManager.compareDate(o1.date, o2.date);
                }
            });
            return list;
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }




}
