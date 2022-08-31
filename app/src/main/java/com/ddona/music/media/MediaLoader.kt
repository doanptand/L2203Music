package com.ddona.music.media

import android.content.Context
import android.provider.MediaStore
import android.util.Log
import com.ddona.music.model.Album
import com.ddona.music.model.Artist
import com.ddona.music.model.Song

//load all song, album, artist from storage
//Single responsibility
class MediaLoader private constructor(private val context: Context) {
    val arrSong = arrayListOf<Song>()
    val arrAlbum = arrayListOf<Album>()
    val arrArtist = arrayListOf<Artist>()

    companion object {


        @Volatile
        private var INSTANCE: MediaLoader? = null

        fun getInstance(context: Context): MediaLoader =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: MediaLoader(context).also { INSTANCE = it }
            }
    }


    init {
        loadSong()
        loadAlbum()
        loadArtist()
    }

    private fun loadSong() {
        val songColumns = arrayOf(
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ARTIST,
        )

        val cursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            songColumns,
            null,
            null,
            null
        )
        if (cursor != null) {
            arrSong.clear()
            val indexData = cursor.getColumnIndex(songColumns[0])
            val indexTitle = cursor.getColumnIndex(songColumns[1])
            val indexDisPlay = cursor.getColumnIndex(songColumns[2])
            val indexDuration = cursor.getColumnIndex(songColumns[3])
            val indexAlbum = cursor.getColumnIndex(songColumns[4])
            val indexAlbumId = cursor.getColumnIndex(songColumns[5])
            val indexArtist = cursor.getColumnIndex(songColumns[6])
            while (cursor.moveToNext()) {
                val data = cursor.getString(indexData)
                val title = cursor.getString(indexTitle)
                val displayName = cursor.getString(indexDisPlay)
                val album = cursor.getString(indexAlbum)
                val albumId = cursor.getString(indexAlbumId)
                val artist = cursor.getString(indexArtist)
                val duration = cursor.getInt(indexDuration)
                arrSong.add(Song(displayName, data, title, album, albumId, artist, duration))
            }
            cursor.close()
        }
        Log.d("doanpt", "Song size: ${arrSong.size}")
    }

    private fun loadAlbum() {
        val columnAlbum = arrayOf(
            MediaStore.Audio.Albums._ID,
            MediaStore.Audio.Albums.ALBUM,
            MediaStore.Audio.Albums.ARTIST,
            MediaStore.Audio.Albums.ALBUM_ART,
            MediaStore.Audio.Albums.NUMBER_OF_SONGS
        )
        val cursor = context.contentResolver
            .query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                columnAlbum, null, null, null
            )
        if (cursor != null) {
            try {
                val indexAlbumID = cursor.getColumnIndex(columnAlbum[0])
                val indexAlbum = cursor.getColumnIndex(columnAlbum[1])
                val indexAlbumArtist = cursor.getColumnIndex(columnAlbum[2])
                val indexAlbumArt = cursor.getColumnIndex(columnAlbum[3])
                val indexNumOfSongs = cursor.getColumnIndex(columnAlbum[4])
                cursor.moveToFirst()
                arrAlbum.clear()
                while (!cursor.isAfterLast) {
                    val albumID = cursor.getInt(indexAlbumID)
                    val albumName = cursor.getString(indexAlbum)
                    val albumArtist = cursor.getString(indexAlbumArtist)
                    val albumArt = cursor.getString(indexAlbumArt)
                    val numofSongs = cursor.getInt(indexNumOfSongs)
                    arrAlbum.add(Album(albumID, albumName, albumArt, albumArtist, numofSongs))
                    cursor.moveToNext()
                }
                cursor.close()
            } catch (e: Exception) {
                Log.e("doanpt", "error load album:${e.message}")
            }
        }

        Log.d("doanpt", "albumn size: ${arrSong.size}")

    }

    private fun loadArtist() {
        val columnArtist = arrayOf(
            MediaStore.Audio.Artists._ID,
            MediaStore.Audio.Artists.ARTIST,
            MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
            MediaStore.Audio.Artists.NUMBER_OF_TRACKS
        )
        val cursor = context.contentResolver
            .query(
                MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                columnArtist, null, null, null
            )
        if (cursor != null) {
            try {
                val indexArtistID = cursor.getColumnIndex(columnArtist[0])
                val indexArtist = cursor.getColumnIndex(columnArtist[1])
                val indexNumOfAlbums = cursor.getColumnIndex(columnArtist[2])
                val indexNumOfTracks = cursor.getColumnIndex(columnArtist[3])
                cursor.moveToFirst()
                arrArtist.clear()
                while (!cursor.isAfterLast) {

                    val idArtist = cursor.getInt(indexArtistID)
                    val artistName = cursor.getString(indexArtist)
                    val numOfAlbums = cursor.getInt(indexNumOfAlbums)
                    val numOfTracks = cursor.getInt(indexNumOfTracks)
                    arrArtist.add(Artist(idArtist, artistName, numOfAlbums, numOfTracks))
                    cursor.moveToNext()


                }
                cursor.close()
            } catch (e: Exception) {
                Log.e("doanpt", "error load artist:${e.message}")
            }
        }
        Log.d("doanpt", "artist size: ${arrSong.size}")
    }

}