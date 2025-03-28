package com.arshman_rehan.i222427_i220965

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Check if the message contains a data payload
        if (remoteMessage.data.isNotEmpty()) {
            val type = remoteMessage.data["type"]  // Should be "new_message" for new message notifications
            if (type == "new_message") {
                val title = remoteMessage.data["title"] ?: "New Message"
                val messageBody = remoteMessage.data["message"] ?: "You have a new message."
                // You can pass extras if needed (e.g., recipientUid, chatId)
                val targetIntent = Intent(this, ChatPage::class.java)
                // Example: If you want to pass recipient UID, include it here:
                // targetIntent.putExtra("recipientUid", remoteMessage.data["recipientUid"])
                sendNotification(title, messageBody, targetIntent)
            }
        }

        // Also handle notification payload if provided
        remoteMessage.notification?.let {
            sendNotification(it.title ?: "ConnectMe", it.body ?: "", Intent(this, ChatPage::class.java))
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Optionally, send the new token to your backend for targeted notifications.
    }

    private fun sendNotification(title: String, messageBody: String, intent: Intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = "ConnectMe_Channel_ID"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.notificationicon) // Replace with your own icon resource
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // For Android Oreo and above, create a notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "ConnectMe Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }
}
