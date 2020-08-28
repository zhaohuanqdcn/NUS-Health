package orbroid.nushealth.nushealthapp.utility;

import java.sql.Time;
import java.util.Date;

public class FormatManager {
    public static String format(int x) {
        return x < 10 ? ("0" + x) : String.valueOf(x);
    }

    public static String format(int y, int m, int d) {
        return y + "-" + FormatManager.format(m) + "-" + FormatManager.format(d);
    }

    public static Time parseTime(String time) {
        return Time.valueOf(time.split(" ")[1]);
    }

    public static String parseDate(String time) {
        return time.split(" ")[0];
    }

    public static int compareDate(String d1, String d2) {
        int diff = Integer.parseInt(d1.substring(0, 4)) - Integer.parseInt(d2.substring(0, 4));
        if (diff != 0)
            return -diff;
        diff = Integer.parseInt(d1.substring(5, 7)) - Integer.parseInt(d2.substring(5, 7));
        if (diff != 0)
            return -diff;
        diff = Integer.parseInt(d1.substring(7, 9)) - Integer.parseInt(d2.substring(7, 9));
        return diff;
    }


}
