package orbroid.nushealth.nushealthapp.entity;

public class Doctor {
    private String email;
    private String tel;
    private String id;
    private String name;
    private String gender;
    private String position;

    public Doctor(String id, String name, String gender, String tel, String pos, String email) {
        this.email = email;
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.tel = tel;
        this.position = pos;
    }

    public String getInfo() {
        return "Gender: " + gender + "\nContact: " + tel;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
