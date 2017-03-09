package ru.org.adons.cblock.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import ru.org.adons.cblock.R;
import ru.org.adons.cblock.ui.main.MainActivity;
import ru.org.adons.cblock.utils.Logging;

public class BlockService extends Service {

    private static final int NOTIFICATION_ID = 201507;
    private final Set<String> phones = new HashSet<>();
    private StateListener listener;
    private TelephonyManager manager;
    private ITelephony telephony;

    public static void enable(Context context) {
        context.startService(new Intent(context, BlockService.class));
    }

    public static void disable(Context context) {
        context.stopService(new Intent(context, BlockService.class));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // TODO: load DB to local variable "phones"
        // register Phone State Listener
        listener = new StateListener();
        manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        manager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
        try {
            Class<TelephonyManager> c = TelephonyManager.class;
            //Class c = Class.forName(manager.getClass().getName());
            Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
            telephony = (ITelephony) m.invoke(manager);
        } catch (Exception e) {
            Logging.d(e.getMessage());
        }
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
                        } catch (Exception e) {
                            Logging.d(e.getMessage());
                        }
                        Logging.d("blocked number:" + incomingNumber);
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
        // set Notification - prevent service from stop by system
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notify)
                .setContentTitle(getApplicationContext().getString(R.string.main_notification_title))
                .setContentText(getApplicationContext().getString(R.string.main_notification_text))
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_MIN);
        // handle notification click
        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        // start notification
        startForeground(NOTIFICATION_ID, mBuilder.build());

        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        // TODO: unregister from callbacks
        // unregister Phone State Listener
        manager.listen(listener, PhoneStateListener.LISTEN_NONE);
        // cancel notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);

        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
