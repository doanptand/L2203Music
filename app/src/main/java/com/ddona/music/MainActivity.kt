package com.ddona.music

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ddona.music.adapter.MusicPagerAdapter
import com.ddona.music.databinding.ActivityMainBinding
import com.ddona.music.media.MediaManager
import com.ddona.music.model.Song
import com.ddona.music.service.MusicService
import com.ddona.music.util.Const
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val adapter = MusicPagerAdapter(this)
        binding.content.viewPager.adapter = adapter
        val tabTitles = arrayOf("Song", "Album", "Artist")
        TabLayoutMediator(binding.content.tabLayout, binding.content.viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
        binding.content.bottomMenu.llRoot.visibility = View.GONE
        startMediaService()
        binding.content.bottomMenu.imvNext.setOnClickListener {
            val nextIntent = Intent(Const.ACT_NEXT)
            sendBroadcast(nextIntent)
        }
        binding.content.bottomMenu.imvPrevious.setOnClickListener {
            val nextIntent = Intent(Const.ACT_PREV)
            sendBroadcast(nextIntent)
        }
        binding.content.bottomMenu.imvPausePlay.setOnClickListener {
            val nextIntent = Intent(Const.ACT_PLAY_PAUSE)
            sendBroadcast(nextIntent)
        }
        binding.content.bottomMenu.llRoot.setOnClickListener {
            val detailIntent = Intent(this, DetailSongActivity::class.java)
            startActivity(detailIntent)
        }
        val updateFilter = IntentFilter()
        updateFilter.addAction(Const.ACT_UPDATE_DATA)
        registerReceiver(updateDataReceiver, updateFilter)
    }

    override fun onDestroy() {
        unregisterReceiver(updateDataReceiver)
        super.onDestroy()
    }

    private val updateDataReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.action?.let {
                if (it == Const.ACT_UPDATE_DATA) {
                    updateBottomLayout(true, MediaManager.songs[MediaManager.songIndex])
                }
            }
        }
    }

    private fun startMediaService() {
        val intent = Intent(this, MusicService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)// sau 5 giây mà không thấy notification 
        } else {
            startService(intent)
        }
    }

    fun updateBottomLayout(isVisibility: Boolean, song: Song?) {
        if (isVisibility) {
            binding.content.bottomMenu.llRoot.visibility = View.VISIBLE
            binding.content.bottomMenu.tvBottomTitleSong.text = song!!.displayName
            binding.content.bottomMenu.tvBottomNameArtist.text = song.artist
            if (MediaManager.mediaPlayer.isPlaying) {
                binding.content.bottomMenu.imvPausePlay.setImageResource(R.drawable.ic_pause)
            } else {
                binding.content.bottomMenu.imvPausePlay.setImageResource(R.drawable.ic_play)
            }
        } else {
            binding.content.bottomMenu.llRoot.visibility = View.GONE
        }
    }
}