package ru.org.adons.cblock.list;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;

import ru.org.adons.cblock.MainActivity;
import ru.org.adons.cblock.R;
import ru.org.adons.cblock.auto.AutoTextItem;

public class IncomingCallLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    private Context context;
    private CursorAdapter adapter;
    public static final String[] CALLS_SUMMARY_PROJECTION = new String[]{
            CallLog.Calls._ID,
            CallLog.Calls.NUMBER,
            CallLog.Calls.DATE
    };
    public static final int COLUMN_NUMBER_INDEX = 1;
    public static final int COLUMN_DATE_INDEX = 2;

    public IncomingCallLoader(Context context, CursorAdapter adapter) {
        this.context = context;
        this.adapter = adapter;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri baseUri = CallLog.Calls.CONTENT_URI;

        String select = "(" + CallLog.Calls.TYPE + " = " + CallLog.Calls.INCOMING_TYPE + ")";
        return new CursorLoader(context, baseUri,
                CALLS_SUMMARY_PROJECTION, select, null,
                CallLog.Calls.DEFAULT_SORT_ORDER);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);

        String phone_number;
        Log.d(MainActivity.LOG_TAG, "---------LIST LOADER---------");
        if (data.moveToFirst()) {
            phone_number = data.getString(1);
            Log.d(MainActivity.LOG_TAG, "phone_number=" + phone_number);
        }
        while (data.moveToNext()) {
            phone_number = data.getString(1);
            Log.d(MainActivity.LOG_TAG, "phone_number=" + phone_number);
        }

        FragmentManager fragmentManager = ((MainActivity) context).getSupportFragmentManager();
        CursorLoaderListFragment listFragment = (CursorLoaderListFragment) fragmentManager.findFragmentById(R.id.main_list_fragment);
        if (listFragment != null) {
            if (listFragment.isResumed()) {
                listFragment.setListShown(true);
            } else {
                listFragment.setListShownNoAnimation(true);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

}
