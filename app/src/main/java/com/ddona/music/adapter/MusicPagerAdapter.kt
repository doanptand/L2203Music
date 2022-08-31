package com.ddona.music.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ddona.music.fragment.AlbumFragment
import com.ddona.music.fragment.ArtistFragment
import com.ddona.music.fragment.SongFragment

class MusicPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SongFragment()
            1 -> AlbumFragment()
            2 -> ArtistFragment()
            else -> {
                throw IllegalArgumentException("Unknown to create fragment for position:$position")
            }
        }
    }
}