package com.ddona.music

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ddona.music.databinding.ActivityDetailSongBinding
import com.ddona.music.media.MediaManager
import com.ddona.music.util.Const

class DetailSongActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailSongBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailSongBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val song = MediaManager.songs[MediaManager.songIndex]
        binding.tvSongName.text = song.displayName
        binding.tvArtistsName.text = song.artist
        binding.sbDetailTime.max = song.duration
        binding.imgNext.setOnClickListener {
            val nextIntent = Intent(Const.ACT_NEXT)
            sendBroadcast(nextIntent)
        }
        binding.imgPrevious.setOnClickListener {
            val nextIntent = Intent(Const.ACT_PREV)
            sendBroadcast(nextIntent)
        }
        binding.imgPlayPause.setOnClickListener {
            val nextIntent = Intent(Const.ACT_PLAY_PAUSE)
            sendBroadcast(nextIntent)
        }
    }
}