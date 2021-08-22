package com.petblowmachine.sspi_admin.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.petblowmachine.sspi_admin.R
import com.petblowmachine.sspi_admin.activity.MainActivity
import com.petblowmachine.sspi_admin.modal.Applic

class NotificationReceiver: FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        getSharedPreferences(Applic.sharedPreferenceName, MODE_PRIVATE).edit().putString("admin_token", token).apply()
    }

    private var mNotificationManager: NotificationManager? = null

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val r = RingtoneManager.getRingtone(applicationContext, notification)
        r.play()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            r.isLooping = false
        }

        val v:Vibrator
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                v = getSystemService(VIBRATOR_MANAGER_SERVICE) as Vibrator
                v.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                v = getSystemService(VIBRATOR_SERVICE) as Vibrator
                v.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
            }
            else -> {
                v = getSystemService(VIBRATOR_SERVICE) as Vibrator
                v.vibrate(200)
            }
        }

        val builder = NotificationCompat.Builder(this, "CHANNEL_ID")
        val resultIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        builder.setSmallIcon(R.drawable.sspi_logo)
        builder.setContentTitle(remoteMessage.notification!!.title)
        builder.setContentText(remoteMessage.notification!!.body)
        builder.setContentIntent(pendingIntent)
        builder.setStyle(
            NotificationCompat.BigTextStyle().bigText(
                remoteMessage.notification!!.body
            )
        )
        builder.setAutoCancel(true)

        mNotificationManager = applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "SSPI"
            val channel = NotificationChannel(
                channelId,
                "New Price Requests",
                NotificationManager.IMPORTANCE_HIGH
            )
            mNotificationManager?.createNotificationChannel(channel)
            builder.setChannelId(channelId)
        }

        mNotificationManager?.notify(100, builder.build())

    }

}