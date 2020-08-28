package orbroid.nushealth.nushealthapp.ui;

import android.app.Activity;
import android.content.Context;
import android.view.Window;

import orbroid.nushealth.nushealthapp.R;

public class TransparentStatusBar {
    public static void set(Activity activity) {
        Window window = activity.getWindow();
        window.setStatusBarColor(activity.getColor(R.color.colorPrimary));
    }
}
