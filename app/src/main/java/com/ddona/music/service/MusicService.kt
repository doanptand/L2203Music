package com.ddona.music.service

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import com.ddona.music.MainActivity
import com.ddona.music.R
import com.ddona.music.media.MediaManager
import com.ddona.music.media.MediaState
import com.ddona.music.util.Const

class MusicService : Service() {
    private lateinit var notificationView: RemoteViews
    private lateinit var notificationBuilder: Notification.Builder

    override fun onCreate() {
        super.onCreate()
        val intentFilter = IntentFilter()
        intentFilter.addAction(Const.ACT_NEXT)
        intentFilter.addAction(Const.ACT_PREV)
        intentFilter.addAction(Const.ACT_PLAY_PAUSE)
        intentFilter.addAction(Const.ACT_UPDATE_DATA)
        registerReceiver(musicReceiver, intentFilter)
    }


    override fun onDestroy() {
        unregisterReceiver(musicReceiver)
        super.onDestroy()
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

    private val musicReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.action?.let {
                when (it) {
                    Const.ACT_NEXT -> nextSong()
                    Const.ACT_PREV -> previousSong()
                    Const.ACT_PLAY_PAUSE -> playPauseSong()
                    Const.ACT_UPDATE_DATA -> updateData()
                }
            }
        }
    }

    private fun updateData() {
        val title = MediaManager.songs[MediaManager.songIndex].displayName
        notificationView.setTextViewText(R.id.tvTitle, title)
        if (MediaManager.mediaPlayer.isPlaying) {
            notificationView.setImageViewResource(R.id.ibPause, R.drawable.ic_pause)
        } else {
            notificationView.setImageViewResource(R.id.ibPause, R.drawable.ic_play)
        }
        startForeground(Const.NOTIFICATION_ID, notificationBuilder.build())
    }

    //broadcast recevier nhận act: next/prev/play/pause và thực hiện action

    private fun runInForeground() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationView = RemoteViews(packageName, R.layout.layout_notification_music_service)


        val nextIntent = Intent(Const.ACT_NEXT)
        val nextPendingIntent =
            PendingIntent.getBroadcast(this, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        notificationView.setOnClickPendingIntent(R.id.ibNext, nextPendingIntent)

        val prevIntent = Intent(Const.ACT_PREV)
        val prevPendingIntent =
            PendingIntent.getBroadcast(this, 0, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        notificationView.setOnClickPendingIntent(R.id.ibPre, prevPendingIntent)

        val playPauseIntent = Intent(Const.ACT_PLAY_PAUSE)
        val playPausePendingIntent =
            PendingIntent.getBroadcast(this, 0, playPauseIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        notificationView.setOnClickPendingIntent(R.id.ibPause, playPausePendingIntent)

        notificationBuilder = Notification.Builder(this)
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

    fun playPauseSong() {
        if (MediaManager.mediaState == MediaState.STATE_IDLE) {
            MediaManager.playPauseSong(true)
        } else {
            MediaManager.playPauseSong(false)
        }
        val intent = Intent(Const.ACT_UPDATE_DATA)
        sendBroadcast(intent)
    }

    fun nextSong() {
        MediaManager.nextSong()
        val intent = Intent(Const.ACT_UPDATE_DATA)
        sendBroadcast(intent)
    }

    fun previousSong() {
        MediaManager.previousSong()
        val intent = Intent(Const.ACT_UPDATE_DATA)
        sendBroadcast(intent)
    }

}