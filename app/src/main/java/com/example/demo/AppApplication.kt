package com.example.demo

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.work.PeriodicWorkRequestBuilder
import com.example.demo.di.appModule
import com.example.demo.manager.NotificationWorker
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import androidx.work.*
import java.util.concurrent.TimeUnit

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Koin Dependency Injection
        val koinModules = listOf(appModule)
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@MyApplication)
            modules(koinModules)
        }

        // Create notification channel for notifications
        createNotificationChannel(this)
        scheduleNotificationWorker(this)
    }

    /**
     * Creates a notification channel for devices running Android 8.0 (API 26) and above.
     * This is required to show notifications on modern Android versions.
     */
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "apps_channel",
                "Apps Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Channel for new apps notifications"
            }
            val notificationManager: NotificationManager =
                context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Schedule the periodic notification worker to trigger notifications every 30 minutes,
     * ensuring it works reliably in the background or when the app is killed on modern Android versions.
     */
    fun scheduleNotificationWorker(context: Context) {
        // Log the start of scheduling process
        Log.d(TAG, "Scheduling NotificationWorker with 30 minutes interval")

        // Create a periodic work request to trigger the NotificationWorker every 30 minutes
        val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
            30, TimeUnit.MINUTES  // Set interval to 30 minutes (minimum for PeriodicWorkRequest is 15)
        )
            .setConstraints(
                Constraints.Builder()
                    .setRequiresBatteryNotLow(false) // Allow work even if battery is low
                    .setRequiredNetworkType(NetworkType.NOT_REQUIRED) // No network requirement
                    .build()
            )
            .build()

        // Enqueue the work uniquely, replacing any existing one with the same name
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "NewAppsNotificationWork",                 // Unique name for this periodic work
            ExistingPeriodicWorkPolicy.REPLACE,        // Replace if work with same name exists
            workRequest                                // The periodic work request
        )

        // Log successful scheduling
        Log.d(TAG, "NotificationWorker scheduled successfully")
    }

    companion object{
        const val TAG = "MyApplication"
    }
}

