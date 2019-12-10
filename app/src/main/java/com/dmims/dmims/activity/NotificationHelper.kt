package com.dmims.dmims.activity

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import com.dmims.dmims.R

class NotificationHelper {

    fun displayNotification(context: Context, title: String, body: String) {

        val intent = Intent(context, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            context,
            100,
            intent, PendingIntent.FLAG_CANCEL_CURRENT
        )
        val mbuilder = NotificationCompat.Builder(context, "Test_Coding")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val mNotifiactionMgr = NotificationManagerCompat.from(context)
        mNotifiactionMgr.notify(1, mbuilder.build())

    }
}