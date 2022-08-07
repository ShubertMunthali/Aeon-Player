package com.infbyte.aeon.models

data class Song (
    val id: Long,
    val title: String,
    val artist: String,
    val album: String,
    val duration: Long,
    val albumId: Int,
    var art: String = "",
    val path: String
    )