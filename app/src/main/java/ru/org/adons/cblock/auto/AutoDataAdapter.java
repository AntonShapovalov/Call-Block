package ru.org.adons.cblock.auto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import ru.org.adons.cblock.R;

public class AutoDataAdapter extends ArrayAdapter<AutoListItem> {

    private Context context;

    private static class ViewHolder {
        TextView phone_number;
        TextView date;
    }

    public AutoDataAdapter(Context context, int resource) {
        super(context, resource);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        // set list item view
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder();
            holder.phone_number = (TextView) convertView.findViewById(R.id.list_item_text1);
            holder.date = (TextView) convertView.findViewById(R.id.list_item_text2);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // set list item values
        holder.phone_number.setText(this.getItem(position).getPhoneNumber());
        holder.date.setText(this.getItem(position).getDate());

        return convertView;
    }

}
