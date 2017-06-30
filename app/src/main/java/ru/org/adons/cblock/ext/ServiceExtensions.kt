package ru.org.adons.cblock.ext

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.support.v4.app.TaskStackBuilder
import ru.org.adons.cblock.R
import ru.org.adons.cblock.ui.activity.MainActivity

/**
 * Extensions for [Service]
 */

private val NOTIFICATION_ID = 201507

fun Service.showNotification() {
    // set Notification - prevent service from stop by system
    val mBuilder = NotificationCompat.Builder(this)
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