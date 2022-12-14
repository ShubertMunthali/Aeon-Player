package com.infbyte.aeon.playback

import android.content.ContentUris
import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.provider.MediaStore
import androidx.annotation.DrawableRes
import com.infbyte.aeon.R
import com.infbyte.aeon.models.Song
import com.infbyte.aeon.playback.AeonMusicPlayer.PlayMode.REPEAT
import com.infbyte.aeon.playback.AeonMusicPlayer.PlayMode.REPEAT_ONE
import com.infbyte.aeon.playback.AeonMusicPlayer.PlayMode.SHUFFLE
import com.infbyte.aeon.preferences.AeonPreferences
import com.infbyte.aeon.ui.listeners.PlaybackListener

class AeonMusicPlayer private constructor(private val context: Context): MediaPlayer(), MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {

    private constructor(context: Context, song: Song): this(context){
            currentSong = song
            initialize()
    }

    private val currentSongList = mutableListOf<Song>()
    private val preferences = AeonPreferences.getInstance(context)

    private fun initialize(){
        setOnCompletionListener(this)
        setOnPreparedListener(this)
        setAudioAttributes(
            AudioAttributes.Builder().
                    setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        )
        prepareSong(currentSong!!)
    }

    fun prepareSong(song: Song){
        currentSong = song
        preferences.storeCurrentSong(currentSong!!)
        val uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, song.id)
        reset()
        setDataSource(context, uri)
        prepare()
    }

    fun setCurrentSongs(songs: List<Song>){
        if(currentSongList.isNotEmpty())
            currentSongList.clear()

        if(songs.isNotEmpty())
            currentSongList.addAll(songs)
    }

    fun play(){
        playbackListener?.onSongPlayed(context){}
    }

    fun playNext(){
        var nextIndex = 0
        if(currentSongList.contains(currentSong)){
            val currentIndex = currentSongList.indexOf(currentSong)
            nextIndex = currentIndex + 1
            if(nextIndex > currentSongList.size)
                nextIndex = 0
        }

        if(nextIndex < currentSongList.size)
            currentSong = currentSongList[nextIndex]

        prepareSong(currentSong!!)
        playbackListener?.onSongPlayed(context){}
    }

    fun playPrevious(){
        var nextIndex = 0
        if(currentSongList.contains(currentSong)){
            val currentIndex = currentSongList.indexOf(currentSong)
            nextIndex = currentIndex - 1
        }
        if(nextIndex < currentSongList.size && nextIndex >= 0) currentSong = currentSongList[nextIndex]
        prepareSong(currentSong!!)
        playbackListener?.onSongPlayed(context){}
    }

    private fun playNext(song: Song){
        currentSong = song
        prepareSong(song)
        playbackListener?.onSongPlayed(context){}
    }

    private fun shuffle(){
        var nextSong = currentSong
        if(currentSongList.isNotEmpty()) nextSong = currentSongList.random()
        if(nextSong != currentSong) currentSong = nextSong
        else shuffle()
        prepareSong(currentSong!!)
        playbackListener?.onSongPlayed(context){}
    }

    private fun repeat(){
        prepareSong(currentSong!!)
        playbackListener?.onSongPlayed(context){}
    }

    override fun onCompletion(mp: MediaPlayer?) {
        if(nextSong != null) {
            playNext(nextSong!!)
            nextSong = null
        }
        else
            when(playMode){
                REPEAT_ONE ->
                    repeat()
                REPEAT ->
                    playNext()
                SHUFFLE ->
                    shuffle()
            }
    }

    override fun onPrepared(mp: MediaPlayer?) {
        return
    }

    object PlayMode{
        @DrawableRes
        const val REPEAT_ONE = R.drawable.repeat_one
        @DrawableRes
        const val REPEAT = R.drawable.repeat
        @DrawableRes
        const val SHUFFLE = R.drawable.shuffle
    }

    companion object{
        @Volatile
        private var musicPlayerInstance: AeonMusicPlayer? = null

        private var playMode = REPEAT

        private var playbackListener: PlaybackListener? = null

        private var currentSong: Song? = null

        private var nextSong: Song? = null

        fun initialize(context: Context, song: Song): AeonMusicPlayer{
            return getInstance(context, song)
        }

        fun getInstance(context: Context): AeonMusicPlayer{
            if(musicPlayerInstance == null)
                musicPlayerInstance = AeonMusicPlayer(context)
            return musicPlayerInstance!!
        }

        private fun getInstance(context: Context, song: Song): AeonMusicPlayer{
            if(musicPlayerInstance == null){
                musicPlayerInstance = AeonMusicPlayer(context, song)
            }
            return musicPlayerInstance!!
        }

        fun setPlaybackListener(listener: PlaybackListener){
            playbackListener = listener
        }

        fun setPlayMode(mode: Int){
            playMode = mode
        }

        fun setNextSong(song: Song?){
            nextSong = song
        }

        fun getCurrentSong() = currentSong!!
    }
}