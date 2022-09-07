package com.infbyte.aeon.preferences

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.PersistableBundle
import com.infbyte.aeon.models.Song
import com.infbyte.aeon.playback.AeonMusicPlayer
import com.infbyte.aeon.playback.AeonMusicPlayer.PlayMode.REPEAT
import com.infbyte.aeon.playback.AeonMusicPlayer.PlayMode.REPEAT_ONE

class AeonPreferences private constructor(context: Context) {

    private val sharedPreferences = context.getSharedPreferences(NAME, MODE_PRIVATE)
    private val prefEditor = sharedPreferences.edit()

    fun storeCurrentSong(song: Song){
        prefEditor.apply{
            putLong(SONG_ID, song.id)
            apply()
        }
    }

    fun getCurrentSongId() = sharedPreferences.getLong(SONG_ID, 0L)

    fun storePlayMode(mode: Int){
        prefEditor.apply{
            putInt(PLAY_MODE, mode)
            apply()
        }
    }

    fun getPlayMode() = sharedPreferences.getInt(PLAY_MODE, REPEAT)

    fun storePlayList(name: String){
        //PersistableBundle().putL
    }

    companion object{
        const val NAME = "aeon_prefs"
        const val SONG_ID = "current_song_id"
        const val PLAY_MODE = "aeon_play_mode"

        private var preferences: AeonPreferences? = null

        fun getInstance(context: Context): AeonPreferences{
            if(preferences == null)
                preferences = AeonPreferences(context)
            return preferences!!
        }
    }
}