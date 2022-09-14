package com.ddona.music

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.ddona.music.databinding.ActivityDetailSongBinding
import com.ddona.music.media.MediaManager
import com.ddona.music.util.Const
import java.io.File

class DetailSongActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailSongBinding
    private val handler = Handler(Looper.getMainLooper())

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

        binding.topMenu.imgShare.setOnClickListener {
            val file = File(MediaManager.songs[MediaManager.songIndex].dataPath)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setType("audio/*")
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file))
            startActivity(Intent.createChooser(intent, "Share to"))
        }

        binding.sbDetailTime.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    MediaManager.mediaPlayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                //start animation
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                //stop animation
            }


        })
        updateUI()
    }


    private fun updateUI() {
        val songIndex = "${MediaManager.songIndex + 1}/${MediaManager.songs.size}"
        binding.tvIndexOfSong.text = songIndex
        val currentTime = MediaManager.mediaPlayer.currentPosition
        val totalTime = MediaManager.mediaPlayer.duration

        binding.tvTimeProgress.text = milliSecondsToTimer(currentTime.toLong())
        binding.tvTimeTotal.text = milliSecondsToTimer(totalTime.toLong())
        binding.sbDetailTime.max = totalTime
        binding.sbDetailTime.progress = currentTime
        handler.postDelayed({ updateUI() }, 500)
    }

    private fun milliSecondsToTimer(milliseconds: Long): String {
        var finalTimerString = ""
        var secondsString = ""
        // Convert total duration into time
        val hours = (milliseconds / (1000 * 60 * 60)).toInt()
        val minutes = (milliseconds % (1000 * 60 * 60)).toInt() / (1000 * 60)
        val seconds = (milliseconds % (1000 * 60 * 60) % (1000 * 60) / 1000).toInt()
        // Add hours if there
        if (hours > 0) {
            finalTimerString = "$hours:"
        }
        // Prepending 0 to seconds if it is one digit
        secondsString = if (seconds < 10) {
            "0$seconds"
        } else {
            "" + seconds
        }
        finalTimerString = "$finalTimerString$minutes:$secondsString"
        // return timer string
        return finalTimerString
    }
}