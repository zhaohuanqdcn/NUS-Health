package orbroid.nushealth.nushealthapp;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import org.json.JSONException;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import orbroid.nushealth.nushealthapp.ui.BannerContent;
import orbroid.nushealth.nushealthapp.ui.GlideImageLoader;
import orbroid.nushealth.nushealthapp.ui.TransparentStatusBar;
import orbroid.nushealth.nushealthapp.utility.Authenticator;
import orbroid.nushealth.nushealthapp.entity.Student;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences loginInfo;
    private String userEmail, userPassword;

    private void initialize() {
        final Button tempDecl = findViewById(R.id.tempDeclare);
        tempDecl.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainActivity.this.startActivityForResult(
                                new Intent(MainActivity.this, TempDecl.class), 0);
                    }
                });
        final Button booking = findViewById(R.id.booking);
        booking.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainActivity.this.startActivityForResult(
                                new Intent(MainActivity.this, BookingHome.class), 0);
                    }
                });
    }

    private void UiSetUp() {
        List<Integer> images = Arrays.asList(BannerContent.IMAGES);
        List<String> titles = Arrays.asList(BannerContent.TITLES);
        final String[] uris = BannerContent.URIS;

        TransparentStatusBar.set(this);

        final Banner banner = findViewById(R.id.banner);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        banner.setImageLoader(new GlideImageLoader());
        banner.setImages(images);
        banner.setBannerTitles(titles);
        banner.setBannerAnimation(Transformer.DepthPage);
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Uri uri = Uri.parse(uris[position]);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        banner.start();
    }

    // fetch from file
    private void getInfo() {
        userEmail = loginInfo.getString("email", "");
        userPassword = loginInfo.getString("password", "");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginInfo = this.getSharedPreferences("nus_health.loginInfo", MODE_PRIVATE);

        getInfo();
        initialize();
        UiSetUp();

        try {
            // auto login succeeded
            if (Authenticator.check(userEmail, userPassword))
                Toast.makeText(
                        this, "Logged in as " + Student.getName(),
                        Toast.LENGTH_SHORT).show();
            // redirect to login page
            else
                MainActivity.this.startActivityForResult(
                        new Intent(MainActivity.this, Login.class), 0);

        } catch (JSONException e) {
            Toast.makeText(
                    this, "Some error occurs :(",
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            getInfo();
        }
    }




}
