package orbroid.nushealth.nushealthapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import orbroid.nushealth.nushealthapp.ui.TransparentStatusBar;

public class BookingDetail extends AppCompatActivity {

    private Button goHome;
    private Button viewBooking;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_detail);

        TransparentStatusBar.set(this);

        androidx.appcompat.widget.Toolbar tb = findViewById(R.id.toolbar_booking_detail);
        tb.setTitle("Booking Detail");
        tb.setLogo(R.mipmap.nus_health_logo_small);
        setSupportActionBar(tb);

        goHome = findViewById(R.id.goHome);
        viewBooking = findViewById(R.id.viewMore);
        text = findViewById(R.id.bookingDetail);

        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BookingDetail.this, MainActivity.class));
            }
        });
        viewBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BookingDetail.this, BookingHistory.class));
            }
        });
        text.setText(getIntent().getStringExtra("detail"));

    }
}


