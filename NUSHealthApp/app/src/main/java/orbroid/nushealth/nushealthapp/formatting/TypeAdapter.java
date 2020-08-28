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

public class TypeAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<BookingType> mList;

    public TypeAdapter(Context context, ArrayList<BookingType> list) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.booking_type_format, null);
            holder.icon = convertView.findViewById(R.id.newTypeIcon);
            holder.name = convertView.findViewById(R.id.newTypeName);
            holder.info = convertView.findViewById(R.id.newTypeInfo);
            convertView.setTag(holder);
        }
        else
            holder = (ViewHolder) convertView.getTag();

        BookingType type = mList.get(position);
        holder.icon.setImageResource(type.typeIcon);
        holder.name.setText(type.typeName);
        holder.info.setText(type.typeInfo);
        return convertView;
    }

    public final class ViewHolder {
        public ImageView icon;
        public TextView name;
        public TextView info;
    }
}
