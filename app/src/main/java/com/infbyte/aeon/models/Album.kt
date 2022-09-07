package com.infbyte.aeon.models

import android.graphics.Bitmap

data class Album (
    val id: Long,
    val title: String,
    //val artist: String,
    val art: Bitmap? = null,
    val numberOfSongs: Int
)