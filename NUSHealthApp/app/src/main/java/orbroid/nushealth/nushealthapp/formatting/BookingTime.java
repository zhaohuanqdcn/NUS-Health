package orbroid.nushealth.nushealthapp.formatting;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import orbroid.nushealth.nushealthapp.entity.Booking;
import orbroid.nushealth.nushealthapp.entity.Doctor;
import orbroid.nushealth.nushealthapp.entity.NewBooking;
import orbroid.nushealth.nushealthapp.entity.Student;
import orbroid.nushealth.nushealthapp.utility.Connector;
import orbroid.nushealth.nushealthapp.utility.FormatManager;
import orbroid.nushealth.nushealthapp.utility.JsonManager;

import static orbroid.nushealth.nushealthapp.utility.Connector.getAvailableSlots;

public class BookingTime {
    private int hour;
    private int minute;
    private int duration;
    private ArrayList<Doctor> doctor;
    private String time;

    public BookingTime(String time, ArrayList<Doctor> doc) {
        this.time = time;
        this.hour = FormatManager.parseTime(time).getHours();
        this.minute = FormatManager.parseTime(time).getMinutes();
        this.duration = NewBooking.type <= 1 ? 20 : 40;
        this.doctor = doc;
    }

    private String getEndTime(){
        int endHour = (hour + (minute + duration) / 60) % 24;
        int endMin = (minute + duration) % 60;
        return FormatManager.format(endHour)+ ":" + FormatManager.format(endMin);
    }

    public String getTime(){
        return FormatManager.format(hour) + ":" + FormatManager.format(minute) + " - " + getEndTime();
    }

    public String getDetailedTime() {
        return this.time;
    }

    public String getDoc(){
        String docNames = "";
        for (Doctor i : doctor)
            docNames += i.getName() + "  ";
        return docNames;
    }

    public ArrayList<Doctor> getDocList() {
        return this.doctor;
    }

    public static ArrayList<BookingTime> getList() {
        ArrayList<BookingTime> list = new ArrayList<>();
        try {
            String history = CompletableFuture.supplyAsync(
                    new Supplier<String>() {
                        @Override
                        public String get() {
                            try {
                                return Connector.getAvailableSlots(Student.getId(),
                                        NewBooking.type, NewBooking.getDate());
                            } catch (IOException e) {
                                e.printStackTrace();
                                return "failed";
                            }
                        }
                    }).join();
            list = JsonManager.slotJson(history);
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
