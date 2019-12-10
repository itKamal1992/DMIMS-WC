package com.dmims.dmims.fcm

import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import com.dmims.dmims.R
import com.dmims.dmims.activity.MainActivity
import com.dmims.dmims.activity.SplashScreen
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyService : FirebaseMessagingService() {
    override fun onMessageReceived(p0: RemoteMessage?) {
        // TODO(developer): Handle FCM messages here.


        if (p0!!.getNotification() != null) {
            val title = p0!!.getNotification()!!.getTitle()
            val body = p0.getNotification()!!.getBody()


            val intent = Intent(applicationContext, SplashScreen::class.java)

            val pendingIntent = PendingIntent.getActivity(
                applicationContext,
                100,
                intent, PendingIntent.FLAG_CANCEL_CURRENT
            )

            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_launcher)
            val mbuilder = NotificationCompat.Builder(applicationContext, "Test_Coding")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            val mNotifiactionMgr = NotificationManagerCompat.from(applicationContext)
            mNotifiactionMgr.notify(1, mbuilder.build())
        }





        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
//        Log.d(TAG, "From: " + p0!!.getFrom());

        // Check if message contains a data payload.
  /*      if (p0!!.data.isNotEmpty()) {
//            Log.d(TAG, "Message data payload: " + p0.getData());

            if (*//* Check if data needs to be processed by long running job *//* true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                //scheduleJob();
            } else {
                // Handle message within 10 seconds
//                handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (p0!!.notification != null) {

            Log.d(TAG, "Message Notification Body: " + p0.notification!!.body);

            println("aaaa  "+p0.notification!!.body)

        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        super.onMessageReceived(p0)*/
    }
    
}
