package ru.org.adons.cblock.auto;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;

public class IncomingCallLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    private Context context;
    private IncomingListDataAdapter adapter;
    public static final String[] CALLS_SUMMARY_PROJECTION = new String[]{
            CallLog.Calls._ID,
            CallLog.Calls.NUMBER,
            CallLog.Calls.DATE
    };
    private static final int COLUMN_NUMBER_INDEX = 1;
    private static final int COLUMN_DATE_INDEX = 2;

    public IncomingCallLoader(Context context, IncomingListDataAdapter adapter) {
        this.context = context;
        this.adapter = adapter;
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri baseUri = CallLog.Calls.CONTENT_URI;

        String select = "(" + CallLog.Calls.TYPE + " = " + CallLog.Calls.INCOMING_TYPE + ")";
        return new CursorLoader(context, baseUri,
                CALLS_SUMMARY_PROJECTION, select, null,
                CallLog.Calls.DEFAULT_SORT_ORDER);

    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        adapter.clear();
        IncomingListItem item;
        String phone_number;
        String date;
        boolean isData = data.moveToFirst();
        while (isData) {
            phone_number = data.getString(IncomingCallLoader.COLUMN_NUMBER_INDEX);
            date = data.getString(IncomingCallLoader.COLUMN_DATE_INDEX);
            item = new IncomingListItem(context, phone_number, date);
            adapter.add(item);
            isData = data.moveToNext();
        }
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        adapter.clear();
    }

}
