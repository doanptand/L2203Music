package com.ddona.music.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ddona.music.adapter.SongAdapter
import com.ddona.music.databinding.FragmentAlbumBinding
import com.ddona.music.databinding.FragmentArtistBinding
import com.ddona.music.media.MediaLoader

class ArtistFragment : Fragment() {
    private lateinit var binding: FragmentArtistBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArtistBinding.inflate(inflater, container, false)
        return binding.root
    }
}