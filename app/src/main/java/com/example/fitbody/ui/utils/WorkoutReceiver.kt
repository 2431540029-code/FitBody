package com.example.fitbody.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.fitbody.R

class WorkoutReceiver : BroadcastReceiver() {

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {
        val message =
            intent.getStringExtra("message")
                ?: "Đến giờ tập luyện rồi 💪"

        val channelId =
            "fitgym_reminder_channel"

        val notificationManager =
            context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel =
                NotificationChannel(
                    channelId,
                    "Nhắc lịch tập",
                    NotificationManager.IMPORTANCE_HIGH
                )

            notificationManager.createNotificationChannel(channel)
        }

        val notification =
            NotificationCompat.Builder(
                context,
                channelId
            )
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("FitGym")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build()

        notificationManager.notify(
            System.currentTimeMillis().toInt(),
            notification
        )
    }
}