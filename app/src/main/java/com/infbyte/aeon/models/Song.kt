package com.infbyte.aeon.models

import android.graphics.Bitmap

data class Song(
    val id: Long,
    val title: String,
    val artist: String,
    val artistId: Long,
    val album: String,
    val albumId: Long,
    val duration: Long,
    var art: String? = null,
    val folder: String,
    val thumbnail: Bitmap? = null
)