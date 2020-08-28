package orbroid.nushealth.nushealthapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import orbroid.nushealth.nushealthapp.entity.Booking;
import orbroid.nushealth.nushealthapp.entity.Student;
import orbroid.nushealth.nushealthapp.ui.TransparentStatusBar;
import orbroid.nushealth.nushealthapp.utility.Authenticator;
import orbroid.nushealth.nushealthapp.utility.Connector;

public class SignUp extends AppCompatActivity {

    private LinearLayout form;
    private Button signUp;
    private TextView email;
    private TextView password1, password2, name, contact, veriCode;
    private RadioGroup gender;
    private String userEmail, userPwd, confirmPwd, stuGender, stuName, stuContact, code;
    private SharedPreferences info;
    private SharedPreferences.Editor editor;

    View.OnClickListener firstListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            userEmail = email.getText().toString();
            userPwd = password1.getText().toString();
            confirmPwd = password2.getText().toString();
            stuContact = contact.getText().toString();
            stuName = name.getText().toString();
            stuGender = "";
            RadioButton g = (RadioButton) gender.getChildAt(1);
            if (g.isChecked())
                stuGender = "Female";
            else {
                g = (RadioButton) gender.getChildAt(0);
                if (g.isChecked())
                    stuGender = "Male";
            }

            // incomplete input
            if (userEmail.equals(""))
                Toast.makeText(
                    SignUp.this, "Please enter your email",
                            Toast.LENGTH_SHORT).show();
            /*
            else if (!userEmail.contains("nus"))
                Toast.makeText(
                        SignUp.this, "Please enter an NUS email.",
                        Toast.LENGTH_SHORT).show();
             */
            else if (userPwd.equals(""))
                Toast.makeText(
                        SignUp.this, "Please enter your password.",
                        Toast.LENGTH_SHORT).show();
            else if (confirmPwd.equals(""))
                Toast.makeText(
                        SignUp.this, "Please confirm your password.",
                        Toast.LENGTH_SHORT).show();
            else if (stuName.equals(""))
                Toast.makeText(
                        SignUp.this, "Please enter your name.",
                        Toast.LENGTH_SHORT).show();
            else if (stuGender.equals(""))
                Toast.makeText(
                        SignUp.this, "Please select your gender",
                        Toast.LENGTH_SHORT).show();
            else if (stuContact.equals(""))
                Toast.makeText(
                        SignUp.this, "Please enter your contact number",
                        Toast.LENGTH_SHORT).show();

            // invalid input
            else if (userPwd.length() < 6 || userPwd.length() > 16)
                Toast.makeText(
                        SignUp.this, "Oops! Your password is invalid",
                        Toast.LENGTH_SHORT).show();
            else if (!userPwd.equals(confirmPwd))
                Toast.makeText(
                        SignUp.this, "Oops! Different password",
                        Toast.LENGTH_SHORT).show();

            // first post
            else {
                String message = CompletableFuture.supplyAsync(
                        new Supplier<String>() {
                            @Override
                            public String get() {
                                try {
                                    return Connector.sendEmail(userEmail);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    return "Error occurs :(";
                                }

                            }
                        }).join();
                Toast.makeText(
                        SignUp.this, message, Toast.LENGTH_SHORT).show();
                if (message.equals("Email sent")) {
                    Toast.makeText(
                            SignUp.this, "Please check your mail box and" +
                                    "\nenter the verification code in 5 min",
                            Toast.LENGTH_LONG).show();
                    form.setVisibility(View.INVISIBLE);
                    veriCode.setVisibility(View.VISIBLE);
                    signUp.setOnClickListener(secondListener);
                }
            }
        }
    };

    View.OnClickListener secondListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            code = veriCode.getText().toString();
            if (code.equals(""))
                Toast.makeText(
                        SignUp.this, "Please enter the verification code",
                        Toast.LENGTH_LONG).show();
            String message = CompletableFuture.supplyAsync(
                    new Supplier<String>() {
                        @Override
                        public String get() {
                            try {
                                return Connector.postNewStudent(userEmail, userPwd, stuName,
                                                                stuGender, stuContact, code);
                            } catch (IOException e) {
                                e.printStackTrace();
                                return "Error occurs :(";
                            }
                        }
                    }).join();
            Toast.makeText(
                    SignUp.this, message, Toast.LENGTH_LONG).show();
            if (message.equals("Sign up successfully")) {
                updateLocal();
                setResult(Activity.RESULT_OK);
                finish();
            }
        }
    };


    private void updateLocal() {
        info = this.getSharedPreferences("nus_health.loginInfo", MODE_PRIVATE);
        editor = info.edit();
        editor.putString("email", userEmail);
        editor.putString("password", userPwd);
        editor.apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page);

        TransparentStatusBar.set(this);

        androidx.appcompat.widget.Toolbar tb = findViewById(R.id.toolbar_login);
        tb.setTitle("Sign Up");
        tb.setLogo(R.mipmap.nus_health_logo_small);
        setSupportActionBar(tb);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        form = findViewById(R.id.signup_form);
        email = findViewById(R.id.email_signup);
        password1 = findViewById(R.id.password_1);
        password2 = findViewById(R.id.password_2);
        contact = findViewById(R.id.contact_signup);
        name = findViewById(R.id.name_signup);
        veriCode = findViewById(R.id.veriCode_signup);
        gender = findViewById(R.id.gender_signup);

        signUp = findViewById(R.id.button_signup);
        signUp.setOnClickListener(firstListener);

    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }
}


