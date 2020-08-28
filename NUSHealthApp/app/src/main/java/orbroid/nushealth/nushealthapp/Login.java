package orbroid.nushealth.nushealthapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import java.util.Objects;

import orbroid.nushealth.nushealthapp.entity.Student;
import orbroid.nushealth.nushealthapp.ui.TransparentStatusBar;
import orbroid.nushealth.nushealthapp.utility.Authenticator;

public class Login extends AppCompatActivity {

    private Button login;
    private TextView signUp, email, password;
    private String userEmail, userPassword;
    private SharedPreferences info;
    private SharedPreferences.Editor editor;

    View.OnClickListener loginListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            userEmail = email.getText().toString();
            userPassword = password.getText().toString();

            // incomplete input
            if (userEmail.equals("") || userPassword.equals(""))
                Toast.makeText(
                    Login.this, "Please enter your email and password",
                            Toast.LENGTH_SHORT).show();
            else {
                try {
                    //login succeeded
                    if (Authenticator.check(userEmail, userPassword)) {
                        // update info in file
                        editor.putString("email", userEmail);
                        editor.putString("password", userPassword);
                        editor.putString("id", Student.getId());
                        editor.apply();
                        Toast.makeText(
                                Login.this, "Logged in as " + Student.getName(),
                                Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                    // login failed
                    else
                        Toast.makeText(
                                Login.this, "Incorrect input\nPlease check again\n",
                                Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    View.OnClickListener signUpListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Login.this.startActivityForResult(new Intent(Login.this, SignUp.class), 0);
        }
    };

    // set stored info
    private void setInfo() {
        userEmail = info.getString("email", "");
        userPassword = info.getString("password", "");
        email.setText(userEmail);
        password.setText(userPassword);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        TransparentStatusBar.set(this);

        androidx.appcompat.widget.Toolbar tb = findViewById(R.id.toolbar_login);
        tb.setTitle("Log in");
        tb.setLogo(R.mipmap.nus_health_logo_small);
        setSupportActionBar(tb);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        info = this.getSharedPreferences("nus_health.loginInfo", MODE_PRIVATE);
        editor = info.edit();

        login = findViewById(R.id.login);
        signUp = findViewById(R.id.signup);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login.setOnClickListener(loginListener);
        signUp.setOnClickListener(signUpListener);

        setInfo();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            setInfo();
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }
}


