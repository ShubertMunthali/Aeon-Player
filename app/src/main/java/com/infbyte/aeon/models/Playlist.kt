package com.infbyte.aeon.models

data class Playlist(
    val name: String,
    val numberOfSongs: Int,
    val songIds: List<Long>
)