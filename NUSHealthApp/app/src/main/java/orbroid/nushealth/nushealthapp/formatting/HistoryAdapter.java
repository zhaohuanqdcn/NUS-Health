package orbroid.nushealth.nushealthapp.formatting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import orbroid.nushealth.nushealthapp.R;
import orbroid.nushealth.nushealthapp.entity.Booking;

public class HistoryAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Booking> mList;

    public HistoryAdapter(Context context, ArrayList<Booking> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.booking_history_format, null);
            holder.bookingHead = convertView.findViewById(R.id.bookingHead);
            convertView.setTag(holder);
        }
        else
            holder = (ViewHolder) convertView.getTag();

        Booking booking = mList.get(position);
        holder.bookingHead.setText(booking.getBookingBrief());
        return convertView;
    }

    public final class ViewHolder {
        public TextView bookingHead;
    }
}
