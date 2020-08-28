package orbroid.nushealth.nushealthapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import orbroid.nushealth.nushealthapp.entity.Student;
import orbroid.nushealth.nushealthapp.ui.TransparentStatusBar;

public class TempDecl extends AppCompatActivity {

    private TextView inputBox;
    private Button reset, submit;
    RadioGroup condition;

    // OnClickListener for Button SUBMIT
    View.OnClickListener submitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (Student.id == -1) {
                startActivityForResult(new Intent(TempDecl.this, Login.class), 0);
                return;
            }
            // get input results
            String tempStr = inputBox.getText().toString();
            String condStr = "";

            RadioButton r = (RadioButton) condition.getChildAt(1);
            if (r.isChecked())
                condStr = "No";
            else {
                r = (RadioButton) condition.getChildAt(0);
                if (r.isChecked())
                    condStr = "Yes";
            }
            // result incomplete
            if (condStr == "")
                Toast.makeText(TempDecl.this, "Please declare whether " +
                        "you have \n additional conditions", Toast.LENGTH_SHORT).show();
            else if (tempStr.isEmpty())
                Toast.makeText(TempDecl.this, "Please declare" +
                        " your temperature", Toast.LENGTH_SHORT).show();
                // result complete
            else{
                double tempValue = Double.parseDouble(tempStr);
                // unreasonable temperature
                if (tempValue < 35.0 || tempValue > 40.0)
                    Toast.makeText(TempDecl.this, "Please check" +
                            " your temperature again", Toast.LENGTH_SHORT).show();
                    // submit
                else {
                    // do something ...
                    Toast.makeText(TempDecl.this, "Declared successfully:\n" +
                            tempStr + ", " + condStr, Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp_decl);

        TransparentStatusBar.set(this);

        androidx.appcompat.widget.Toolbar tb = findViewById(R.id.toolbar_temp_decl);
        tb.setTitle("Temperature");
        tb.setLogo(R.mipmap.nus_health_logo_small);
        setSupportActionBar(tb);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        inputBox = findViewById(R.id.tempInput);

        condition = findViewById(R.id.condition);

        reset = findViewById(R.id.tempReset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputBox.setText("");
                condition.clearCheck();
            }
        });

        submit = findViewById(R.id.tempSubmit);
        submit.setOnClickListener(submitListener);

    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }
}


