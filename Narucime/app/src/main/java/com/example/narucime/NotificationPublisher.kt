package com.example.narucime

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotificationPublisher : BroadcastReceiver() {
    companion object {

        var NOTIFICATION_ID = "notificationId"
        var NOTIFICATION = "notification"
        var NOTIFICATON_TITLE = "notificationId"
        var NOTIFICATION_CONTENT = "notificationContent"
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("Hoj", "Bok ja sam filip")

        val id = intent.getIntExtra(NOTIFICATION_ID, 0)
        val title = intent.getIntExtra(NOTIFICATON_TITLE, 0)
        val content = intent.getIntExtra(NOTIFICATION_CONTENT, 0)

        val channelId = "Channel+${id}"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, "Notification", importance).apply {
                description = "Hi, I am notificaton!"
            }

            val notificationManager: NotificationManager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(MyApplication.ApplicationContext, channelId)
            .setContentTitle("Bok")
            .setContentText("Ja sam Filip")
            .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(MyApplication.ApplicationContext)) {
            notify(id, builder.build())
        }

    }
}