package ru.qwerty.bank

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * @author y.yangel
 */

class PushNotificationService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        message.notification?.let {
            createNotification(
                title = it.title,
                message = it.body,
            )
        }
        super.onMessageReceived(message)
    }

    @SuppressLint("MissingPermission")
    private fun createNotification(title: String?, message: String?) {
        val notificationId = 1001
        val channelId = "my_channel_id"
        val channelName = "My Channel"

        val channelDescription = "Channel description"
        val channelImportance = NotificationManager.IMPORTANCE_DEFAULT

        val notificationChannel =
            NotificationChannel(channelId, channelName, channelImportance).apply {
                description = channelDescription
            }

        getSystemService(NotificationManager::class.java).createNotificationChannel(
            notificationChannel
        )

        val builder = NotificationCompat.Builder(this, "my_channel_id")
            .setSmallIcon(com.example.feature.account.ui.R.drawable.transfer_between)
            .setContentTitle(title ?: "Новое уведомление")
            .setContentText(message ?: "У вас новое сообщение")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        builder.setContentIntent(pendingIntent)
        NotificationManagerCompat.from(this).notify(notificationId, builder.build())
    }
}