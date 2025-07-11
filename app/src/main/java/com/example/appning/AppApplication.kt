package com.example.appning

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.appning.di.appModule
import com.example.appning.manager.NewAppsNotificationWorker
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import java.util.concurrent.TimeUnit

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Koin Dependency Injection with application module(s)
        val koinModules = listOf(appModule)
        startKoin {
            androidLogger(Level.DEBUG)               // Enable debug logs for Koin
            androidContext(this@MyApplication)       // Provide application context to Koin
            modules(koinModules)                     // Load app's DI modules
        }

        // Create notification channel (required for Android 8.0+)
        createNotificationChannel()

        // Schedule periodic background work for new apps notification
        setNotificationAlarm()
    }

    /**
     * Creates a notification channel for displaying new apps notifications.
     * Notification channels are required for Android 8.0 (API 26) and above.
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "apps_channel",                           // Channel ID
                "New Apps Notification",                  // Channel name (visible to users)
                NotificationManager.IMPORTANCE_DEFAULT    // Importance level
            ).apply {
                description = "Notifies when new apps are available."
            }

            // Register the channel with the system notification manager
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Schedules a periodic WorkManager task to check for new apps every 30 minutes.
     * If a periodic task with the same name already exists, it will be kept.
     */
    private fun setNotificationAlarm() {
        val workRequest = PeriodicWorkRequestBuilder<NewAppsNotificationWorker>(
            30, TimeUnit.MINUTES // Minimum allowed interval for periodic work
        ).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "apps_notification_work",                  // Unique work name
            ExistingPeriodicWorkPolicy.KEEP,           // Keep existing work if already scheduled
            workRequest
        )
    }
}
