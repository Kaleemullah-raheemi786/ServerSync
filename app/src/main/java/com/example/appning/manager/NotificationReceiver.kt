package com.example.appning.manager

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.appning.MainActivity
import com.example.appning.R
import java.util.concurrent.TimeUnit

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        Log.d("NotificationReceiver", "onReceive triggered — preparing to send notification.")

        val notificationManager = NotificationManagerCompat.from(context)

        // Check if app has POST_NOTIFICATIONS permission (Android 13+)
        val hasPostNotificationPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true // No permission required on lower versions
        }

        // Check if notifications are enabled and permission granted
        if (notificationManager.areNotificationsEnabled() && hasPostNotificationPermission) {
            Log.d("NotificationReceiver", "Notifications are enabled and permission granted.")

            // Intent to open the MainActivity when notification is tapped
            val openIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent = PendingIntent.getActivity(
                context, 0, openIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            // Build the notification
            val notification = NotificationCompat.Builder(context, "apps_channel")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("New apps available!")
                .setContentText("Check out the latest apps in the store.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

            // Trying sending the notification
            try {
                notificationManager.notify(System.currentTimeMillis().toInt(), notification)
                Log.d("NotificationReceiver", "Notification sent successfully.")
            } catch (e: SecurityException) {
                Log.e("NotificationReceiver", "Failed to send notification: ${e.message}")
                e.printStackTrace()
            }
        } else {
            Log.w("NotificationReceiver", "Notifications disabled or POST_NOTIFICATIONS permission not granted.")
        }

        // Schedule the next alarm to trigger after 15 minutes
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val repeatIntent = Intent(context, NotificationReceiver::class.java)
        val repeatPendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            repeatIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val intervalMillis = 30 * 60 * 1000L
        val nextTriggerTime = System.currentTimeMillis() + intervalMillis

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // For Android 12+ check canScheduleExactAlarms() before scheduling
            if (alarmManager.canScheduleExactAlarms()) {
                try {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        nextTriggerTime,
                        repeatPendingIntent
                    )
                    Log.d("NotificationReceiver", "Exact alarm scheduled for: $nextTriggerTime")
                } catch (e: SecurityException) {
                    Log.e("NotificationReceiver", "Failed to schedule exact alarm: ${e.message}")
                    e.printStackTrace()
                }
            } else {
                Log.w("NotificationReceiver", "Cannot schedule exact alarms — permission not granted.")
            }
        } else {
            // For Android 11 and below, directly schedule alarm
            try {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    nextTriggerTime,
                    repeatPendingIntent
                )
                Log.d("NotificationReceiver", "Exact alarm scheduled for: $nextTriggerTime (Legacy)")
            } catch (e: SecurityException) {
                Log.e("NotificationReceiver", "Failed to schedule exact alarm (Legacy): ${e.message}")
                e.printStackTrace()
            }
        }
    }
}






