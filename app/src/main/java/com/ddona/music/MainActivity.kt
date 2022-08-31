package com.ddona.music

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ddona.music.adapter.MusicPagerAdapter
import com.ddona.music.databinding.ActivityMainBinding
import com.ddona.music.service.MusicService
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
        startMediaService()
    }

    private fun startMediaService() {
        val intent = Intent(this, MusicService::class.java)
        startService(intent)
    }
}