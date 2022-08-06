package com.infbyte.aeon.ui.listeners

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.infbyte.aeon.models.Song
import com.infbyte.aeon.playback.AeonMusicPlayer
import com.infbyte.aeon.ui.activities.AeonMainActivity

interface  PlaybackListener {

    fun onSongPlayed(context: Context) {
        AeonMusicPlayer.getInstance(context).start()
    }

    fun onSongPaused(context: Context) {
        AeonMusicPlayer.getInstance(context).pause()
    }

    fun onSongSkippedToNext(context: Context) {
         AeonMusicPlayer.getInstance(context).playNext()
    }

    fun onSongSkippedToPrevious(context: Context){
        AeonMusicPlayer.getInstance(context).playPrevious()
    }
}

fun AppCompatActivity.onSongChanged(song: (Song) -> Unit) {
    song(AeonMusicPlayer.getInstance(this).getCurrentSong())
}