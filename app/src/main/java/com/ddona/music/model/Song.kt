package com.ddona.music.model

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore

data class Song(
    val displayName: String,
    val dataPath: String,
    val title: String,
    val album: String,
    val albumId: String,
    val artist: String,
    val duration: Int
) {
    fun getImageSong(context: Context): Bitmap? {
        val sArtworkUri = Uri
            .parse("content://media/external/audio/albumart")
        val albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId.toLong())
        var bitmap: Bitmap? = null
        try {
            bitmap = MediaStore.Images.Media.getBitmap(
                context.contentResolver, albumArtUri
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bitmap
    }
}
