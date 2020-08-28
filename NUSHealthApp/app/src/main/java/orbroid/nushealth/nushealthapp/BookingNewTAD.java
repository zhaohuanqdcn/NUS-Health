package orbroid.nushealth.nushealthapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Objects;

import orbroid.nushealth.nushealthapp.entity.Booking;
import orbroid.nushealth.nushealthapp.entity.Student;
import orbroid.nushealth.nushealthapp.formatting.BookingType;
import orbroid.nushealth.nushealthapp.entity.NewBooking;
import orbroid.nushealth.nushealthapp.formatting.TypeAdapter;
import orbroid.nushealth.nushealthapp.ui.TransparentStatusBar;
import orbroid.nushealth.nushealthapp.utility.FormatManager;

public class BookingNewTAD extends AppCompatActivity {

    private ArrayList<BookingType> list;
    private DatePickerDialog datePickerDialog;
    private TextView date;
    private Button cont;
    private Spinner sp;
    private Calendar dateSelect =  Calendar.getInstance();

    Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
    int mYear = c.get(Calendar.YEAR);
    int mMonth = c.get(Calendar.MONTH);
    int mDay = c.get(Calendar.DAY_OF_MONTH);

    private void initSpinner() {
        list = BookingType.getList();
        TypeAdapter adapter = new TypeAdapter(this, list);
        sp.setAdapter(adapter);
        sp.setSelection(0);
    }

    private boolean checkDate() {
        int diff = dateSelect.compareTo(c);
        if (diff < 0) {
            Toast.makeText(BookingNewTAD.this, "Please "
                            + "select a date in the future", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            Calendar sevenDaysLater = Calendar.getInstance();
            sevenDaysLater.add(Calendar.DATE, 7);
            if (dateSelect.compareTo(sevenDaysLater) >= 0) {
                Toast.makeText(BookingNewTAD.this, "Please "
                                + "select a date nearer", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }

    private void initCalendar() {
        datePickerDialog = new DatePickerDialog(BookingNewTAD.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        dateSelect.set(year, monthOfYear, dayOfMonth);
                        if (checkDate())
                            date.setText("Date: " + FormatManager.format(year, monthOfYear+1, dayOfMonth));
                    }
                }, mYear, mMonth, mDay);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_new_tad);

        TransparentStatusBar.set(this);

        androidx.appcompat.widget.Toolbar tb = findViewById(R.id.toolbar_booking_new_tad);
        tb.setTitle("Type & Time");
        tb.setLogo(R.mipmap.nus_health_logo_small);
        setSupportActionBar(tb);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        sp = findViewById(R.id.typeSpinner);
        date = findViewById(R.id.dateSelect);
        cont = findViewById(R.id.continue_BookingNewTAD);
        date.setText("Date: " + FormatManager.format(mYear, mMonth+1, mDay));
        initSpinner();
        initCalendar();

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Student.id == -1) {
                    startActivityForResult(new Intent(BookingNewTAD.this, Login.class), 0);
                    return;
                }
                if (checkDate()) {
                    NewBooking.date = dateSelect;
                    NewBooking.type = (int) sp.getSelectedItemId();
                    startActivity(new Intent(BookingNewTAD.this, BookingNewTime.class));
                }
            }
        });

    }


    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }

}


