package eamato.funn.r6companion.core.notifications

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import eamato.funn.r6companion.R
import eamato.funn.r6companion.core.DEFAULT_NOTIFICATION_CHANNEL_ID
import eamato.funn.r6companion.core.extenstions.isPermissionGranted
import eamato.funn.r6companion.ui.activities.ActivityMain

object R6NotificationManager {

    fun showNotification(context: Context, notificationId: Int, title: String?, content: String?) {
        with(NotificationManagerCompat.from(context)) {
            @SuppressLint("MissingPermission")
            if (context.isPermissionGranted(Manifest.permission.POST_NOTIFICATIONS)) {
                notify(notificationId, createNotification(context, title, content))
            }
        }
    }

    fun createNotificationChannel(
        context: Context,
        notificationChannelName: String,
        notificationChannelDescription: String
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannelImportance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(
                DEFAULT_NOTIFICATION_CHANNEL_ID,
                notificationChannelName,
                notificationChannelImportance
            ).apply { description = notificationChannelDescription }

            val notificationManager =
                ContextCompat.getSystemService(context, NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }

    private fun createNotification(
        context: Context,
        title: String?,
        content: String?
    ): Notification {
        val intent = Intent(context, ActivityMain::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, FLAG_IMMUTABLE)

        return NotificationCompat.Builder(context, DEFAULT_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
    }

}