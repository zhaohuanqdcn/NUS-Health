package orbroid.nushealth.nushealthapp.formatting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import orbroid.nushealth.nushealthapp.R;
import orbroid.nushealth.nushealthapp.entity.Doctor;

public class DoctorAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Doctor> list;

    public DoctorAdapter(Context context, ArrayList<Doctor> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.booking_doctor_format, null);
            holder.name = convertView.findViewById(R.id.newDoctorName);
            holder.info = convertView.findViewById(R.id.newDoctorInfo);
            convertView.setTag(holder);
        }
        else
            holder = (ViewHolder) convertView.getTag();

        Doctor doc = list.get(position);
        holder.name.setText(doc.getName());
        holder.info.setText(doc.getInfo());
        return convertView;
    }

    public final class ViewHolder {
        public TextView name;
        public TextView info;
    }

}
