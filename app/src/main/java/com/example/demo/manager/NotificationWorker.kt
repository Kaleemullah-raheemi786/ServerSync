package com.example.demo.manager

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.demo.MainActivity
import com.example.demo.R


class NotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    companion object {
        const val TAG = "NotificationWorker"
    }

    override suspend fun doWork(): Result {
        try {
            val context = applicationContext
            val notificationManager = NotificationManagerCompat.from(context)

            // Check notification permission for Android 13+
            val hasPostNotificationPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                true
            }

            if (notificationManager.areNotificationsEnabled() && hasPostNotificationPermission) {
                Log.d(TAG, "Notifications enabled and permission granted, sending notification.")

                // Intent to open MainActivity on notification tap
                val openIntent = Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                val pendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    openIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                val notification = NotificationCompat.Builder(context, "apps_channel")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("New apps available!")
                    .setContentText("Check out the latest apps in the store.")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build()

                notificationManager.notify(System.currentTimeMillis().toInt(), notification)
                Log.d(TAG, "Notification sent successfully.")
            } else {
                Log.w(TAG, "Notifications disabled or POST_NOTIFICATIONS permission not granted.")
            }
            return Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Error in NotificationWorker: ${e.message}", e)
            return Result.retry()
        }
    }
}







