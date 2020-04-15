package com.example.narucime.Notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.narucime.Context.MyApplication
import com.example.narucime.R
import kotlin.random.Random

class NotificationPublisher : BroadcastReceiver() {
    companion object {

        var NOTIFICATION_ID = "notificationId"
        var NOTIFICATION = "notification"
        var NOTIFICATON_TITLE = "notificationId"
        var NOTIFICATION_CONTENT = "notificationContent"
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("Hoj", "Bok ja sam filip")
        val n_ID = intent.getStringExtra(
            NotificationPublisher.NOTIFICATION_ID ?: "nothing recieved")
        val n_TITLE = intent.getStringExtra(
            NotificationPublisher.NOTIFICATON_TITLE ?: "nothing recieved")
        val n_CONTENT = intent.getStringExtra(
            NotificationPublisher.NOTIFICATION_CONTENT ?: "nothing recieved")

        val id = intent.getIntExtra(NOTIFICATION_ID, 0)
        val title = intent.getStringExtra(NOTIFICATON_TITLE)
        val content = intent.getStringExtra(NOTIFICATION_CONTENT)

        val randomId = Random.nextInt(1, 10000)

        val channelId = "Channel${id}"

        Log.d("chanelID", n_ID.toString())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(randomId.toString(), "Notificationnn${randomId}", importance).apply {
                description = "Hi, I am notificaton!"
            }

            val notificationManager: NotificationManager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(MyApplication.ApplicationContext, channelId)
            .setContentTitle(n_TITLE)
            .setContentText(n_CONTENT)
            .setSmallIcon(R.mipmap.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(MyApplication.ApplicationContext)) {
            notify(randomId, builder.build())
        }

    }
}