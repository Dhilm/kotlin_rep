package com.example.serviceexample

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat

class MyService : Service() {

    private lateinit var soundPlayer: MediaPlayer
    private val CHANNEL_ID = "channelId"
    private lateinit var notifManager: NotificationManager

    override fun onCreate() {
        super.onCreate()
        Toast.makeText(this, "Service Created", Toast.LENGTH_SHORT).show()
        soundPlayer = MediaPlayer.create(this, R.raw.song)
        soundPlayer.isLooping = false
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val offerChannelName = "Service Channel"
            val offerChannelDescription = "Music Channel"
            val offerChannelImportance = NotificationManager.IMPORTANCE_DEFAULT

            val notifChannel = NotificationChannel(CHANNEL_ID, offerChannelName, offerChannelImportance).apply {
                description = offerChannelDescription
            }
            notifManager = getSystemService(NotificationManager::class.java)
            notifManager.createNotificationChannel(notifChannel)
        }

        val sNotifBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_music)
            .setContentTitle("My Music Player")
            .setContentText("Music is playing")

        val servNotification: Notification = sNotifBuilder.build()
        startForeground(1, servNotification)

        soundPlayer.start()
        return START_STICKY
    }

    override fun onDestroy() {
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_SHORT).show()
        soundPlayer.stop()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}