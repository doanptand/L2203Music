package com.ddona.music.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ddona.music.adapter.SongAdapter
import com.ddona.music.databinding.FragmentSongBinding
import com.ddona.music.media.MediaLoader

class SongFragment : Fragment() {
    private lateinit var binding: FragmentSongBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSongBinding.inflate(inflater, container, false)
        binding.lvSong.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            Log.d("doanpt", "fragment Song size:${MediaLoader.getInstance(requireContext()).arrSong.size}")
            adapter = SongAdapter(MediaLoader.getInstance(requireContext()).arrSong)
        }
        return binding.root
    }
}