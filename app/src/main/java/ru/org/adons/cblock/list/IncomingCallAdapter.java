package ru.org.adons.cblock.list;

import android.content.ContentResolver;
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

public class IncomingCallAdapter extends CursorAdapter implements Filterable {

    private ContentResolver content;

    public IncomingCallAdapter(Context context) {
        super(context, null, false);
        content = context.getContentResolver();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(
                android.R.layout.simple_list_item_2, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final TextView tv1 = (TextView) view.findViewById(android.R.id.text1);
        tv1.setText(cursor.getString(IncomingCallLoader.COLUMN_NUMBER_INDEX));
        final TextView tv2 = (TextView) view.findViewById(android.R.id.text2);
        Date date = new Date(Long.parseLong(cursor.getString(IncomingCallLoader.COLUMN_DATE_INDEX)));
        tv2.setText(DateFormat.getDateFormat(context).format(date) + DateFormat.getTimeFormat(context).format(date));
    }

    @Override
    public String convertToString(Cursor cursor) {
        return cursor.getString(IncomingCallLoader.COLUMN_NUMBER_INDEX);
    }

/*
    @Override
    public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
        Log.d(MainActivity.LOG_TAG, "LIST ADAPTER:runQueryOnBackgroundThread");
        Uri uri = CallLog.Calls.CONTENT_URI;
        String select = "(" + CallLog.Calls.TYPE + " = " + CallLog.Calls.INCOMING_TYPE + ")";
        return content.query(uri, IncomingCallLoader.CALLS_SUMMARY_PROJECTION, select, null, CallLog.Calls.DEFAULT_SORT_ORDER);
    }
*/

}
