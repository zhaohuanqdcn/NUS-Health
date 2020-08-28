package orbroid.nushealth.nushealthapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Objects;

import orbroid.nushealth.nushealthapp.entity.Doctor;
import orbroid.nushealth.nushealthapp.entity.NewBooking;
import orbroid.nushealth.nushealthapp.formatting.BookingTime;
import orbroid.nushealth.nushealthapp.formatting.DoctorAdapter;
import orbroid.nushealth.nushealthapp.formatting.TimeAdapter;
import orbroid.nushealth.nushealthapp.ui.TransparentStatusBar;

public class BookingNewDoctor extends AppCompatActivity {
    private TextView hint;
    private ArrayList<Doctor> list;
    private GridView lv;

    private void initGridView() {
        list = NewBooking.time.getDocList();
        DoctorAdapter adapter = new DoctorAdapter(this, list);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Doctor doc = (Doctor) lv.getItemAtPosition(position);
                NewBooking.doctor = doc;
                startActivity(new Intent(BookingNewDoctor.this, BookingNewConfirm.class));
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_new_doctor);

        TransparentStatusBar.set(this);

        androidx.appcompat.widget.Toolbar tb = findViewById(R.id.toolbar_booking_new_doc);
        tb.setTitle("Doctor");
        tb.setLogo(R.mipmap.nus_health_logo_small);
        setSupportActionBar(tb);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        lv = findViewById(R.id.gridView_bookingNewDoctor);
        hint = findViewById(R.id.text_bookingNewDoctor);

        hint.setText("Type: " + NewBooking.getType() +
                    "\nDate: " + NewBooking.getDate() +
                    "\nTime: " + NewBooking.getTime());

        initGridView();

    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }
}


