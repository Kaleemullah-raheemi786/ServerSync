package com.example.appning.manager

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.NotificationCompat
import com.example.appning.MainActivity
import com.example.appning.R

// Worker class to show a notification about new apps using WorkManager and coroutines
class NewAppsNotificationWorker(
    context: Context, params: WorkerParameters
) : CoroutineWorker(context, params) {

    // This is the entry point for WorkManager tasks
    override suspend fun doWork(): Result {
        // Trigger the notification when work runs
        sendNotification()

        // Indicate work completed successfully
        return Result.success()
    }

    // Function to build and send a notification
    private fun sendNotification() {
        // Check if the POST_NOTIFICATIONS permission is granted (required for Android 13+)
        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Get the NotificationManager
            val notificationManager = NotificationManagerCompat.from(applicationContext)

            // Create an intent to open MainActivity when the notification is tapped
            val intent = Intent(applicationContext, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

            // Create a PendingIntent wrapping the above intent
            val pendingIntent = PendingIntent.getActivity(
                applicationContext,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            // Build the notification using NotificationCompat
            val notification = NotificationCompat.Builder(applicationContext, "apps_channel")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("New apps available!")
                .setContentText("Check out the latest apps in the store.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

            // Show the notification using a unique ID
            notificationManager.notify(System.currentTimeMillis().toInt(), notification)

        } else {
            // Log an error if notification permission not granted
            Log.e("Notification", "Notification permission not granted.")
        }
    }
}


