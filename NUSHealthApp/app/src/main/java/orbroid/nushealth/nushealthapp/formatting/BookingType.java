package orbroid.nushealth.nushealthapp.formatting;

import java.util.ArrayList;

import orbroid.nushealth.nushealthapp.R;

public class BookingType {
    String typeName;
    String typeInfo;
    int typeIcon;

    private BookingType(String name, String info, int icon) {
        this.typeName = name;
        this.typeInfo = info;
        this.typeIcon = icon;
    }

    public static ArrayList<BookingType> getList() {
        String infoA =  "Type A is a 20-minute booking, aiming at admin issues like file submission, file collection etc.";
        String infoB =  "Type B is a 20-minute booking, aiming at minor symptoms like cough, sore throat, mouth ulcer, stomach ache etc.";
        String infoC =  "Type C is a 40-minute booking, aiming at moderate issues like headache, influenza, bandage renewal etc.";
        String infoD =  "Type D is a 40-minute booking, mainly aiming at mental health issues like anxiety, stress, traumatic disorder etc.";
        ArrayList<BookingType> list = new ArrayList<>();
        list.add(new BookingType("Type A", infoA, R.mipmap.file_icon));
        list.add(new BookingType("Type B", infoB, R.mipmap.thermometer_icon));
        list.add(new BookingType("Type C", infoC, R.mipmap.pill_icon));
        list.add(new BookingType("Type D", infoD, R.mipmap.heart_icon));
        return list;
    }
}
