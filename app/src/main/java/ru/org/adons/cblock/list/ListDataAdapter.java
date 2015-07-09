package ru.org.adons.cblock.list;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.Date;

import ru.org.adons.cblock.R;
import ru.org.adons.cblock.db.PhonesTable;

public class ListDataAdapter extends CursorAdapter implements Filterable {

    public ListDataAdapter(Context context) {
        super(context, null, false);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // number
        final TextView tv1 = (TextView) view.findViewById(R.id.list_item_text1);
        tv1.setText(cursor.getString(PhonesTable.COLUMN_NUMBER_INDEX));
        // name
        final TextView tv2 = (TextView) view.findViewById(R.id.list_item_text2);
        tv2.setText(cursor.getString(PhonesTable.COLUMN_NAME_INDEX));
        // date
        final TextView tv4 = (TextView) view.findViewById(R.id.list_item_text4);
        Date date = new Date(Long.parseLong(cursor.getString(PhonesTable.COLUMN_DATE_INDEX)));
        tv4.setText(DateFormat.getDateFormat(context).format(date) + " " + DateFormat.getTimeFormat(context).format(date));
    }

}
