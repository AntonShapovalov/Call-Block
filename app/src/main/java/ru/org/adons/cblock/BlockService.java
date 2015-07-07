package ru.org.adons.cblock;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import ru.org.adons.cblock.db.DBContentProvider;
import ru.org.adons.cblock.db.PhonesTable;

public class BlockService extends Service implements Loader.OnLoadCompleteListener<Cursor> {

    private CursorLoader loader;
    private Set<String> phones = new HashSet<String>();
    private StateListener listener;
    private TelephonyManager manager;
    private ITelephony telephony;

    @Override
    public void onCreate() {
        super.onCreate();
        // load DB to local variable
        loader = new CursorLoader(getApplicationContext(), DBContentProvider.CONTENT_URI,
                PhonesTable.PHONES_SUMMARY_PROJECTION, null, null, PhonesTable.DEFAULT_SORT_ORDER);
        loader.registerListener(1, this);
        loader.startLoading();
        // register Phone State Listener
        listener = new StateListener();
        manager = (TelephonyManager) getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        manager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
        try {
            Class c = Class.forName(manager.getClass().getName());
            Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
            telephony = (ITelephony) m.invoke(manager);
        } catch (Exception e) {
            Log.e(MainActivity.LOG_TAG, e.getLocalizedMessage());
        }
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

    private class StateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    if (telephony != null && phones.contains(incomingNumber)) {
                        try {
                            telephony.endCall();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        Log.d(MainActivity.LOG_TAG, "blocked number:" + incomingNumber);
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    break;
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(MainActivity.LOG_TAG, "Service Started");
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
        // unregister Phone State Listener
        manager.listen(listener, PhoneStateListener.LISTEN_NONE);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
