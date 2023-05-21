package hr.medick.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import hr.medick.NewReminderActivity
import hr.medick.R

class PopupNotificationService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Display the pop-up notification
        showPopupNotification()

        // Stop the service after displaying the notification
        stopSelf()

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun showPopupNotification() {
        // Create and display the notification as a pop-up
        // Use a high-priority notification to increase visibility

        val channelId = "1"
        val channelName = "Popup Channel"
        createNotificationChannel(channelId, channelName)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Podjsetnik za uzimanje lijeka")
            .setContentText(
                "Vrijeme za uzeti lijek " + NewReminderActivity.newPodsjetnik.terapija?.lijek +
                        " u dozi od " + NewReminderActivity.newPodsjetnik.terapija?.dozalijeka
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notificationBuilder.build())
    }

    private fun createNotificationChannel(channelId: String, channelName: String) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(channel)
    }
}