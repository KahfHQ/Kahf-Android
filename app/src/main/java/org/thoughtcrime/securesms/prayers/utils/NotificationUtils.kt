package org.thoughtcrime.securesms.prayers.utils

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import org.thoughtcrime.securesms.R
import java.util.Date
import java.util.concurrent.TimeUnit

object NotificationUtils {

    private const val CHANNEL_ID = "PrayerChannelId"
    private const val CHANNEL_NAME = "PrayerChannelName"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun createNotification(context: Context, prayerTime: String?, notificationId: Int) {
        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Prayer Time")
            .setContentText(prayerTime)
            .setSmallIcon(R.drawable.kahf_icon)
        // Add other notification configuration here as needed

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        notificationManager?.notify(notificationId, notificationBuilder.build())
    }

    fun scheduleNotification(
        context: Context,
        prayerTime: String,
        notificationTimeInMillis: Long,
        notificationId: Int
    ) {
        val inputData = Data.Builder()
            .putString("prayerTime", prayerTime)
            .putInt("notificationId", notificationId)
            .build()

        val prayerWork = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInputData(inputData)
            .setInitialDelay(notificationTimeInMillis - System.currentTimeMillis(), TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork("unique_work_$notificationId", ExistingWorkPolicy.REPLACE, prayerWork)
    }

    fun cancelNotification(context: Context, notificationId: Int) {
        WorkManager.getInstance(context).cancelUniqueWork("unique_work_$notificationId")
    }
}