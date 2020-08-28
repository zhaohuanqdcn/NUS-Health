package orbroid.nushealth.nushealthapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Objects;

import orbroid.nushealth.nushealthapp.entity.Booking;
import orbroid.nushealth.nushealthapp.entity.Student;
import orbroid.nushealth.nushealthapp.formatting.HistoryAdapter;
import orbroid.nushealth.nushealthapp.ui.TransparentStatusBar;

public class BookingHistory extends AppCompatActivity {

    private ArrayList<Booking> list;
    private GridView lv;
    private TextView emptyText;

    private void initGridView() {
        list = Booking.getList();
        if (list.size() == 0) {
            emptyText.setVisibility(View.VISIBLE);
            return;
        }
        HistoryAdapter adapter = new HistoryAdapter(this, list);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Booking booking = (Booking) lv.getItemAtPosition(position);
                Intent intent = new Intent(BookingHistory.this, BookingDetail.class);
                intent.putExtra("detail", booking.getBookingDetail());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_history);

        TransparentStatusBar.set(this);

        androidx.appcompat.widget.Toolbar tb = findViewById(R.id.toolbar_booking_history);
        tb.setTitle("Booking History");
        tb.setLogo(R.mipmap.nus_health_logo_small);
        setSupportActionBar(tb);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        lv = findViewById(R.id.gridView_bookingHistory);
        emptyText = findViewById(R.id.emptyText_history);

        if (Student.id == -1) {
            startActivityForResult(new Intent(BookingHistory.this, Login.class), 0);
            return;
        }

        initGridView();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            initGridView();
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }
}