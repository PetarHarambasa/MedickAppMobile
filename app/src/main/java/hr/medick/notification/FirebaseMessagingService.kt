package hr.medick.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import hr.medick.NewReminderActivity
import hr.medick.R

class FirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Handle the received FCM message
        if (remoteMessage.notification != null) {
            // Extract notification data and display the notification
            val title = remoteMessage.notification?.title
            val body = remoteMessage.notification?.body

            // Call a method to display the notification
            showPopupNotification(title, body)
        }
    }

    private fun showPopupNotification(title: String?, body: String?) {
        // Create and display the notification as a pop-up
        // Use a high-priority notification to increase visibility

        val channelId = "1"
        val channelName = "Popup Channel"
        createNotificationChannel(channelId, channelName)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notificationBuilder.build())
    }

    private fun createNotificationChannel(channelId: String, channelName: String) {
        val channel =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}