package com.infbyte.aeon.models

import android.graphics.Bitmap

data class Artist (
    val id: Long,
    val name: String,
    val numberOfSongs: Int,
    val art: Bitmap? = null
)