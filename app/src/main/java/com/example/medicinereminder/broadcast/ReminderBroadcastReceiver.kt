package com.example.medicinereminder.broadcast

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.medicinereminder.R
import com.example.medicinereminder.ui.MainActivity

class ReminderBroadcastReceiver : BroadcastReceiver() {

    // to display medicine name and description in notification
    private lateinit var medicineName: String
    private lateinit var description: String

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) return

        // Retrieve the medicine name and description from the intent extras
        medicineName = intent?.getStringExtra("medicineName") ?: ""
        description = intent?.getStringExtra("description") ?: ""

        // Check for the VIBRATE permission
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.VIBRATE
        ) == PackageManager.PERMISSION_GRANTED

        // Show the notification only if the VIBRATE permission is granted
        if (hasPermission) {
            // Create a notification channel for Android 8.0 (Oreo) and above
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channelId = "reminder_channel"
                val channelName = "Reminder Channel"
                val channelDescription = "Channel for reminders"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(channelId, channelName, importance).apply {
                    description = channelDescription
                }

                // Register the channel with the system
                val notificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }

            // Create the notification
            val notificationBuilder = NotificationCompat.Builder(context, "reminder_channel")
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(medicineName)
                .setContentText("It's time to take your medicine!\n $description")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)

            // Set the content intent to open MainActivity
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            val contentIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            notificationBuilder.setContentIntent(contentIntent)

            // Show the notification
            val notificationManagerCompat =
                NotificationManagerCompat.from(context)
            notificationManagerCompat.notify(0, notificationBuilder.build())
        }
    }
}
