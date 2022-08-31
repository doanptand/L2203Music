package com.ddona.music.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.ddona.music.MainActivity
import com.ddona.music.R
import com.ddona.music.media.MediaManager
import com.ddona.music.media.MediaState
import com.ddona.music.util.Const

class MusicService : Service() {


    override fun onCreate() {
        super.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder {
        return MusicBinder()
    }

    inner class MusicBinder : Binder() {
        fun getMusicService(): MusicService {
            return this@MusicService
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("doanpt", "Service started")
        //START_STICKY khi service bị kill, mà hệ thống đủ tài nguyên thì service sẽ tự động được chạy lại với intent=null
        //START_REDELIVER_INTENT khi service bị kill, mà hệ thống đủ tài nguyên thì service sẽ tự động được chạy lại với intent=gần nhất
        //START_NOT_STICKY khi service bị kill, service sẽ không khởi chạy lại
        runInForeground()
        return START_STICKY
    }

    private fun runInForeground() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationView = RemoteViews(packageName, R.layout.layout_notification_music_service)

        val notificationBuilder = Notification.Builder(this)
        notificationBuilder.setContentTitle("Music app")
        notificationBuilder.setContentText("This is music app")
        notificationBuilder.setSmallIcon(R.drawable.music_icon)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            notificationBuilder.setCustomContentView(notificationView)
        }

        val clickIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        notificationBuilder.setContentIntent(clickIntent)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "music",
                "music app",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
            notificationBuilder.setChannelId(channel.id)
        }
        val notification = notificationBuilder.build()

        startForeground(Const.NOTIFICATION_ID, notification)
//        notificationManager.notify(Const.NOTIFICATION_ID, notification)
    }

    fun playPauseSong(index: Int) {
        MediaManager.songIndex = index
        if (MediaManager.mediaState == MediaState.STATE_IDLE) {
            MediaManager.playPauseSong(true)
        } else {
            MediaManager.playPauseSong(false)
        }
    }

    fun nextSong() {
        MediaManager.nextSong()
    }

    fun previousSong() {
        MediaManager.previousSong()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}