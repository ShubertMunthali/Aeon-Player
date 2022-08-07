package com.infbyte.aeon.ui.listeners

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.infbyte.aeon.models.Song
import com.infbyte.aeon.playback.AeonMusicPlayer
import com.infbyte.aeon.ui.activities.AeonMainActivity

interface  PlaybackListener {

    fun onSongPlayed(context: Context, player: (AeonMusicPlayer) -> Unit) {
        val aeonPlayer = AeonMusicPlayer.getInstance(context)
        aeonPlayer.start()
        //AeonMusicPlayer.isPlaying.value = aeonPlayer.isPlaying
        player(aeonPlayer)
    }

    fun onSongPaused(context: Context, player: (AeonMusicPlayer) -> Unit) {
        val aeonPlayer = AeonMusicPlayer.getInstance(context)
        aeonPlayer.pause()
        //AeonMusicPlayer.isPlaying.value = aeonPlayer.isPlaying
        player(aeonPlayer)
    }

    fun onSongSkippedToNext(context: Context) {
         AeonMusicPlayer.getInstance(context).playNext()
    }

    fun onSongSkippedToPrevious(context: Context){
        AeonMusicPlayer.getInstance(context).playPrevious()
    }

    fun onSongChanged(song: (Song) -> Unit) {
        song(AeonMusicPlayer.getCurrentSong())
    }
}
