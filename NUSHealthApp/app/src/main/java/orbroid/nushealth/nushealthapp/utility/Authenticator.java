package orbroid.nushealth.nushealthapp.utility;

import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import orbroid.nushealth.nushealthapp.entity.Student;

public class Authenticator {

    public static boolean check (final String e, final String pwd) throws JSONException {
        if (e.equals("") || pwd.equals(""))
            return false;
        // fetch json from url
        String info = CompletableFuture.supplyAsync(
                new Supplier<String>() {
                    @Override
                    public String get() {
                        try {
                            return Connector.getStudentInfo(e, pwd);
                        } catch (IOException e) {
                            e.printStackTrace();
                            return "failed";
                        }
                    }
                }).join();

        if (JsonManager.studentJson(info)) {
            Student.setPassword(e, pwd);
            return true;
        }
        else {
            Student.clear();
            return false;
        }
    }
}
