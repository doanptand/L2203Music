package com.ddona.music.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ddona.music.databinding.ItemSongBinding
import com.ddona.music.model.Song

class SongAdapter(private val songs: List<Song>) : RecyclerView.Adapter<SongAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemSongBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(song: Song) {
            binding.tvItemNameSong.text = song.displayName
            binding.tvItemArtistSong.text = song.artist
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        songs[position].let {
            holder.bind(it)
        }
    }

    override fun getItemCount() = songs.size
}