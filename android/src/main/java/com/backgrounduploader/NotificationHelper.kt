package com.backgrounduploader

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

object NotificationHelper {
    private const val CHANNEL_ID = "background_upload_channel"
    private const val CHANNEL_NAME = "Background Upload"
    private const val CHANNEL_DESCRIPTION = "Notifications for background file uploads"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESCRIPTION
            }
            
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showUploadProgress(context: Context, fileName: String, progress: Int) {
        createNotificationChannel(context)
        
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.stat_sys_upload)
            .setContentTitle("Uploading $fileName")
            .setContentText("Upload in progress...")
            .setProgress(100, progress, false)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)

        with(NotificationManagerCompat.from(context)) {
            notify(fileName.hashCode(), builder.build())
        }
    }

    fun showUploadSuccess(context: Context, fileName: String) {
        createNotificationChannel(context)
        
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.stat_sys_upload_done)
            .setContentTitle("Upload Complete")
            .setContentText("$fileName uploaded successfully")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(fileName.hashCode(), builder.build())
        }
    }

    fun showUploadError(context: Context, fileName: String, error: String) {
        createNotificationChannel(context)
        
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.stat_notify_error)
            .setContentTitle("Upload Failed")
            .setContentText("Failed to upload $fileName: $error")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(fileName.hashCode(), builder.build())
        }
    }

    fun cancelNotification(context: Context, fileName: String) {
        with(NotificationManagerCompat.from(context)) {
            cancel(fileName.hashCode())
        }
    }
}