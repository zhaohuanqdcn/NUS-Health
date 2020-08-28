package orbroid.nushealth.nushealthapp.formatting;

import android.content.Context;
import android.provider.SyncStateContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import orbroid.nushealth.nushealthapp.R;

public class TimeAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<BookingTime> list;

    public TimeAdapter(Context context, ArrayList<BookingTime> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.booking_time_format, null);
            holder.time = convertView.findViewById(R.id.newTimeName);
            holder.doctors = convertView.findViewById(R.id.newTimeInfo);
            convertView.setTag(holder);
        }
        else
            holder = (ViewHolder) convertView.getTag();

        BookingTime bTime = list.get(position);
        holder.time.setText(bTime.getTime());
        holder.doctors.setText(bTime.getDoc());
        return convertView;
    }

    public final class ViewHolder {
        public TextView time;
        public TextView doctors;
    }

}
