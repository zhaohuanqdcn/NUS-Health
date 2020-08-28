package orbroid.nushealth.nushealthapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import orbroid.nushealth.nushealthapp.entity.Booking;
import orbroid.nushealth.nushealthapp.entity.Doctor;
import orbroid.nushealth.nushealthapp.entity.NewBooking;
import orbroid.nushealth.nushealthapp.formatting.DoctorAdapter;
import orbroid.nushealth.nushealthapp.ui.TransparentStatusBar;
import orbroid.nushealth.nushealthapp.utility.Connector;

public class BookingNewConfirm extends AppCompatActivity {
    private TextView hint;
    private Button goBack, confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_new_confirm);

        TransparentStatusBar.set(this);

        androidx.appcompat.widget.Toolbar tb = findViewById(R.id.toolbar_booking_new_confirm);
        tb.setTitle("Booking Confirmation");
        tb.setLogo(R.mipmap.nus_health_logo_small);
        setSupportActionBar(tb);

        hint = findViewById(R.id.text_bookingNewConfirm);
        goBack = findViewById(R.id.return_bookingNewConfirm);
        confirm = findViewById(R.id.confirm_bookingNewConfirm);

        hint.setText(NewBooking.getBookingDetail());
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = CompletableFuture.supplyAsync(
                        new Supplier<Boolean>() {
                            @Override
                            public Boolean get() {
                                try {
                                    return Connector.postNewBooking();
                                } catch (IOException | JSONException e) {
                                    e.printStackTrace();
                                    return false;
                                }
                            }
                        }).join();
                if (result) {
                    Toast.makeText(BookingNewConfirm.this, "Booked successfully", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(BookingNewConfirm.this, BookingDetail.class);
                    intent.putExtra("detail", NewBooking.getBookingDetail());
                    startActivity(intent);
                }
                else
                    Toast.makeText(BookingNewConfirm.this, "Booking failed", Toast.LENGTH_SHORT).show();
            }
        });



    }
}


