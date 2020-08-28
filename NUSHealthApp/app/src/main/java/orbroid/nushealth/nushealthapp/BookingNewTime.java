package orbroid.nushealth.nushealthapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Objects;

import orbroid.nushealth.nushealthapp.entity.NewBooking;
import orbroid.nushealth.nushealthapp.formatting.BookingTime;
import orbroid.nushealth.nushealthapp.formatting.BookingType;
import orbroid.nushealth.nushealthapp.formatting.TimeAdapter;
import orbroid.nushealth.nushealthapp.formatting.TypeAdapter;
import orbroid.nushealth.nushealthapp.ui.TransparentStatusBar;

public class BookingNewTime extends AppCompatActivity {
    private TextView hint, emptyText;
    private ArrayList<BookingTime> list;
    private GridView lv;

    private void initGridView() {
        list = BookingTime.getList();
        if (list.size() == 0) {
            emptyText.setVisibility(View.VISIBLE);
            return;
        }
        TimeAdapter adapter = new TimeAdapter(this, list);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BookingTime bookingTime = (BookingTime) lv.getItemAtPosition(position);
                NewBooking.time = bookingTime;
                startActivity(new Intent(BookingNewTime.this, BookingNewDoctor.class));
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_new_time);

        TransparentStatusBar.set(this);

        androidx.appcompat.widget.Toolbar tb = findViewById(R.id.toolbar_booking_new_time);
        tb.setTitle("Time Slot");
        tb.setLogo(R.mipmap.nus_health_logo_small);
        setSupportActionBar(tb);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        lv = findViewById(R.id.gridView_bookingNewTime);
        hint = findViewById(R.id.text_bookingNewTime);
        emptyText = findViewById(R.id.emptyText_newTime);

        hint.setText("Type: " + NewBooking.getType() +
                    "\nDate: " + NewBooking.getDate());

        initGridView();
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }

}


