package mobile.addons.cblock.ext

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.support.v4.app.TaskStackBuilder
import mobile.addons.cblock.R
import mobile.addons.cblock.ui.activity.MainActivity
import android.app.NotificationChannel
import android.os.Build

/**
 * Extensions for [Service]
 */
private const val NOTIFICATION_ID = 201507
private const val CHANNEL_ID = "BLOCK_CHANNEL_201805"

fun Service.showNotification() {
    // for Android 8 create channel
    createNotificationChannel(applicationContext)

    // set Notification - prevent service from stop by system
    val mBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notify)
            .setContentTitle(getString(R.string.main_notification_title))
            .setContentText(getString(R.string.main_notification_text_enable))
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_MIN)

    // handle notification click
    val resultIntent = Intent(this, MainActivity::class.java)
    val stackBuilder = TaskStackBuilder.create(this).apply {
        addParentStack(MainActivity::class.java)
        addNextIntent(resultIntent)
    }
    val resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
    mBuilder.setContentIntent(resultPendingIntent)

    // start notification
    startForeground(NOTIFICATION_ID, mBuilder.build())
}

fun Service.hideNotification(){
    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.cancel(NOTIFICATION_ID)
}

private fun createNotificationChannel(context: Context) {
    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is new and not in the support library
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, "Incoming Call Block Channel", importance)
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }
}