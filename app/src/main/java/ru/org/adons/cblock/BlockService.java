package ru.org.adons.cblock;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ru.org.adons.cblock.db.DBContentProvider;
import ru.org.adons.cblock.db.PhonesTable;

public class BlockService extends Service implements Loader.OnLoadCompleteListener<Cursor> {

    private CursorLoader loader;
    private List<String> phones = new ArrayList<String>();

    @Override
    public void onCreate() {
        super.onCreate();
        loader = new CursorLoader(getApplicationContext(), DBContentProvider.CONTENT_URI,
                PhonesTable.PHONES_SUMMARY_PROJECTION, null, null, PhonesTable.DEFAULT_SORT_ORDER);
        loader.registerListener(1, this);
        loader.startLoading();
    }

    @Override
    public void onLoadComplete(Loader<Cursor> loader, Cursor data) {
        phones.clear();
        boolean isData = data.moveToFirst();
        while (isData) {
            phones.add(data.getString(PhonesTable.COLUMN_NUMBER_INDEX));
            isData = data.moveToNext();
        }
        StringBuilder sb = new StringBuilder();
        for (String s : phones) {
            sb.append(s).append(";");
        }
        Log.d(MainActivity.LOG_TAG, "Service Updated:" + sb.toString());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        StringBuilder sb = new StringBuilder();
        for (String s : phones) {
            sb.append(s).append(";");
        }
        Log.d(MainActivity.LOG_TAG, "Service Started:" + sb.toString());
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        // Stop the cursor loader
        if (loader != null) {
            loader.unregisterListener(this);
            loader.cancelLoad();
            loader.stopLoading();
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
