package orbroid.nushealth.nushealthapp.entity;

public class Student {
    public static int id = -1;
    public static String email, name, gender, tel;
    private static String password;

    public static String getEmail() {
        return email;
    }

    public static void setPassword(String eml, String pwd) {
        email = eml;
        password = pwd;
    }

    public static String getId() {
        return String.valueOf(id);
    }

    public static String getName() {
        return name;
    }

    public static void clear() {
        email = null;
        id = -1;
        name = null;
        gender = null;
        tel = null;
        password = null;
    }
}
