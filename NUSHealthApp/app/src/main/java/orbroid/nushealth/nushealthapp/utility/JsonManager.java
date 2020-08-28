package orbroid.nushealth.nushealthapp.utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.ArrayList;

import okhttp3.FormBody;
import orbroid.nushealth.nushealthapp.entity.*;
import orbroid.nushealth.nushealthapp.formatting.BookingTime;

public class JsonManager {

    public static boolean studentJson(String str) throws JSONException {
        if (str.equals("failed"))
            throw new JSONException("invalid json string");
        JSONObject stu = new JSONObject(str);
        try {
            int id = stu.getInt("num");
            String name = stu.getString("name");
            String gender = stu.getString("gender");
            String tel = stu.getString("contact");
            Student.name = name;
            Student.id = id;
            Student.gender = gender;
            Student.tel = tel;
            return true;
        } catch (JSONException e) {
            throw e;
        }
    }

    public static ArrayList<Doctor> doctorJson(JSONArray allDoctor) throws JSONException{
        ArrayList<Doctor> result = new ArrayList<>();
        try {
            int n = allDoctor.length();
            for (int i = 0; i < n; i++) {
                JSONObject d = allDoctor.getJSONObject(i);
                String id = d.getString("num");
                String name = d.getString("name");
                String gender = d.getString("gender");
                String tel = d.getString("contact");
                String position = d.getString("pos");
                String email = d.getString("email");
                Doctor doctor = new Doctor(id, name, gender, tel, position, email);
                result.add(doctor);
            }
        } catch (JSONException e) {
            throw e;
        }
        return result;
    }

    public static ArrayList<Booking> bookingJson(String str) throws JSONException {
        ArrayList<Booking> result = new ArrayList<>();
        try {
            JSONArray allBookings =  new JSONArray(str);
            int n = allBookings.length();
            for (int i = 0; i < n; i++) {
                JSONObject b = allBookings.getJSONObject(i);
                int id = b.getInt("num");
                String timeStr = b.getString("time");
                int dur = b.getInt("duration");
                int docNum = b.getInt("docNum");
                int type = b.getInt("type");
                Booking booking = new Booking(id, timeStr, dur, docNum, Student.id, type);
                result.add(booking);
            }
        } catch (JSONException e) {
            throw e;
        }
        return result;
    }

    public static ArrayList<BookingTime> slotJson(String str) throws JSONException {
        ArrayList<BookingTime> result = new ArrayList<>();
        try {
            JSONArray allBookingTime = new JSONArray(str);
            int n = allBookingTime.length();
            for (int i = 0; i < n; i++) {
                JSONObject b = allBookingTime.getJSONObject(i);
                String start = b.getString("Start");
                JSONArray doc = b.getJSONArray("Doctors");
                BookingTime bookingTime = new BookingTime(start, doctorJson(doc));
                result.add(bookingTime);
            }
        } catch (JSONException e) {
            throw e;
        }
        return result;
    }

    public static FormBody createNewBooking() {
        FormBody formBody = new FormBody.Builder()
                .add("BookingDocNum", NewBooking.doctor.getId())
                .add("BookingStuNum", Student.getId())
                .add("BookingTime", NewBooking.time.getDetailedTime())
                .add("BookingType", String.valueOf(NewBooking.type))
                .build();
        return formBody;
    }

    public static FormBody createStudentByEmail(String email) {
        FormBody formBody = new FormBody.Builder()
                .add("StuEmail", email)
                .build();
        return formBody;
    }

    public static FormBody createStudent(String email, String pwd, String name,
                                         String gender, String contact, String vericode) {
        FormBody formBody = new FormBody.Builder()
                .add("StuContact", contact)
                .add("StuEmail", email)
                .add("StuGender", gender)
                .add("StuName", name)
                .add("StuPwd", pwd)
                .add("VeriCode", vericode)
                .build();
        return formBody;
    }

}
