package com.ddona.music.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class MusicService : Service() {


    override fun onCreate() {
        super.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("doanpt", "Service started")
        //START_STICKY khi service bị kill, mà hệ thống đủ tài nguyên thì service sẽ tự động được chạy lại với intent=null
        //START_REDELIVER_INTENT khi service bị kill, mà hệ thống đủ tài nguyên thì service sẽ tự động được chạy lại với intent=gần nhất
        //START_NOT_STICKY khi service bị kill, service sẽ không khởi chạy lại
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}