package ru.org.adons.cblock.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import ru.org.adons.cblock.R;
import ru.org.adons.cblock.app.BlockManager;
import ru.org.adons.cblock.app.CBlockApplication;
import ru.org.adons.cblock.data.BlockListModel;
import ru.org.adons.cblock.ui.activity.MainActivity;
import ru.org.adons.cblock.utils.Logging;
import ru.org.adons.cblock.utils.SubscriptionUtils;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class BlockService extends Service {

    private static final int NOTIFICATION_ID = 201507;
    private static volatile boolean isEnabled = false;

    private final CompositeSubscription subscription = new CompositeSubscription();
    private final Set<String> phones = new HashSet<>();

    private StateListener listener;
    private TelephonyManager manager;
    private ITelephony telephony;
    private AudioManager audio;

    @Inject BlockListModel blockListModel;
    @Inject BlockManager blockManager;

    /**
     * Start Block Service in foreground mode
     */
    public static synchronized void start(Context context) {
        if (!isEnabled) {
            isEnabled = true;
            context.startService(new Intent(context, BlockService.class));
        }
    }

    /**
     * Stop Block Service
     */
    public static synchronized void stop(Context context) {
        isEnabled = false;
        context.stopService(new Intent(context, BlockService.class));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // inject dependencies
        CBlockApplication application = (CBlockApplication) getApplication();
        ServiceComponent serviceComponent = DaggerServiceComponent.builder()
                .applicationComponent(application.applicationComponent())
                .build();
        serviceComponent.inject(this);

        // get blocked phones list
        Subscription dataSubscription = blockListModel.getBlockedPhones()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setPhones);
        subscription.add(dataSubscription);

        // subscribe to block list update from BlockManager
        Subscription updateSubscription = blockManager.getBlockListUpdate()
                .map(blockListModel::getBlockedPhones)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setPhones);
        subscription.add(updateSubscription);

        // register Phone State Listener
        listener = new StateListener();
        manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        manager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
        try {
            Class<TelephonyManager> c = TelephonyManager.class;
            Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
            telephony = (ITelephony) m.invoke(manager);
        } catch (Exception e) {
            Logging.d(e.getMessage());
        }

        // init AudioManager
        audio = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
    }

    private class StateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    if (phones.contains(incomingNumber)) {
                        // mute
                        int ringState = audio.getRingerMode();
                        boolean isMuted = false;
                        if (ringState != AudioManager.RINGER_MODE_SILENT) {
                            try {
                                audio.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                                isMuted = true;
                            } catch (Exception e) {
                                Logging.d(e.getMessage());
                            }
                        }

                        // try to end call
                        if (telephony != null) {
                            try {
                                telephony.endCall();
                            } catch (Exception e) {
                                Logging.d(e.getMessage());
                            }
                        }

                        // un-mute
                        if (isMuted) {
                            audio.setRingerMode(ringState);
                        }
                        Logging.d("blocked number:" + incomingNumber);
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    break;
                default:
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
                .setContentText(getApplicationContext().getString(R.string.main_notification_text_enable))
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
        // unsubscribe from all data update
        SubscriptionUtils.unsubscribe(subscription);

        // unregister Phone State Listener
        manager.listen(listener, PhoneStateListener.LISTEN_NONE);

        // cancel notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);

        // stop service
        synchronized (this) {
            isEnabled = false;
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void setPhones(Set<String> values) {
        phones.clear();
        phones.addAll(values);
    }

}
