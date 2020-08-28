package orbroid.nushealth.nushealthapp.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import orbroid.nushealth.nushealthapp.BookingNewConfirm;
import orbroid.nushealth.nushealthapp.entity.Booking;
import orbroid.nushealth.nushealthapp.entity.NewBooking;
import orbroid.nushealth.nushealthapp.entity.Student;

public class Connector {

    private static ConnectivityManager cm;
    private static String path = "https://orbidroid-backend-test-2.herokuapp.com/";

    public static boolean checkConnection(Context context) {
        cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null)
            return false;
        NetworkInfo net = cm.getActiveNetworkInfo();
        return net != null && net.isConnectedOrConnecting();
    }


    public static String getStudentInfo(String stuEmail, String stuPwd) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(path + "student/getByEmailWithPwd?StuEmail=" + stuEmail
                    + "&StuPwd=" + stuPwd).build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful())
            return response.body().string();
        else
            return "failed";
    }

    public static String getBookingHistory(int stuNum) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(path + "booking/getHistory/stu?StuNum=" + stuNum).build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static String getAvailableSlots(String stuNum, int type, String date) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(path + "booking/getAvl?StuNum=" + stuNum
                        + "&BookingType=" + type + "&Date=" + date).build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static boolean postNewBooking() throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient();
        FormBody formBody = JsonManager.createNewBooking();
        Request request = new Request.Builder().url(path + "booking/add").post(formBody).build();
        Response response = client.newCall(request).execute();
        Booking confirmedBooking = JsonManager.bookingJson('[' + response.body().string() + ']').get(0);
        NewBooking.bookingId = confirmedBooking.getId();
        return response.isSuccessful();
    }

    public static String sendEmail(String email) throws IOException {
        OkHttpClient client = new OkHttpClient();
        FormBody formBody = JsonManager.createStudentByEmail(email);
        Request request = new Request.Builder()
                            .url(path + "student/add/email").post(formBody).build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static String postNewStudent(String email, String pwd, String name,
                                         String gender, String contact, String veriCode) throws IOException {
        OkHttpClient client = new OkHttpClient();
        FormBody formBody = JsonManager.createStudent(email, pwd, name, gender, contact, veriCode);
        Request request = new Request.Builder()
                            .url(path + "student/add/email/after").post(formBody).build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }


}
