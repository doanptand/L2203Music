package com.ddona.music.media

import android.media.MediaPlayer
import com.ddona.music.model.Song

enum class MediaState {
    STATE_IDLE, STATE_PLAYING, STATE_PAUSE
}

//play/pause songs
object MediaManager {
    //array songs that playing
    private val songs = arrayListOf<Song>()
    val mediaPlayer = MediaPlayer()
    var songIndex = 0 //bài hát đang được play
    var mediaState = MediaState.STATE_IDLE //quản lí state hiện tại của media


    fun setSongs(temp: List<Song>) {
        songs.clear()
        songs.addAll(temp)
    }

    fun nextSong() {
        if (songIndex < songs.size - 1) {
            songIndex++
        } else {
            songIndex = 0
        }
        playPauseSong(true) //chạy 1 bài mới
    }

    fun previousSong() {
        if (songIndex > 0) {
            songIndex--
        } else {
            songIndex = songs.size - 1
        }
        playPauseSong(true) //chạy 1 bài mới
    }

    fun playPauseSong(isNew: Boolean) {
        if (mediaState == MediaState.STATE_IDLE || isNew) {
            //nếu đang chưa chạy hoặc isNew là chạy mới -> chạy từ đầu
            mediaPlayer.reset()
            mediaPlayer.setDataSource(songs[songIndex].dataPath)
            mediaPlayer.prepare()
            mediaPlayer.start()
            mediaState = MediaState.STATE_PLAYING
        } else if (mediaState == MediaState.STATE_PLAYING) {
            //pause bài hát lại
            mediaPlayer.pause()
            mediaState = MediaState.STATE_PAUSE
        } else if (mediaState == MediaState.STATE_PAUSE) {
            //chạy tiếp bài hát
            mediaPlayer.start()
            mediaState = MediaState.STATE_PLAYING
        }
    }

    fun stopSong() {
        if (mediaState == MediaState.STATE_IDLE) {
            return
        }
        mediaPlayer.stop()
        mediaState = MediaState.STATE_IDLE
    }
}