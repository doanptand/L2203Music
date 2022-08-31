package com.ddona.music.model

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore

data class Artist(
    val artistId: Int,
    val artistName: String?,
    val numberOfAlbums: Int,
    val numberOfTracks: Int,
) {
    fun getImageSong(context: Context): Bitmap? {
        val sArtworkUri = Uri
            .parse("content://media/external/audio/albumart")
        val albumArtUri =
            ContentUris.withAppendedId(sArtworkUri, artistId.toLong())
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