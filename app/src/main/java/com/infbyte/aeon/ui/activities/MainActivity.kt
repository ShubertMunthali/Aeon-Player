package com.infbyte.aeon.ui.activities

import android.content.Context
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.infbyte.aeon.R
import com.infbyte.aeon.playback.AeonMusicPlayer
import com.infbyte.aeon.ui.fragments.dialogs.SongDialog
import com.infbyte.aeon.ui.listeners.PlaybackListener
import com.infbyte.aeon.viewmodels.AeonMusicViewModel

open class MainActivity : AppCompatActivity(), PlaybackListener {

    val songsViewModel: AeonMusicViewModel by viewModels()
    lateinit var playPause: ImageView
    lateinit var skipNext: ImageView
    lateinit var skipPrevious: ImageView
    lateinit var progressBar: ProgressBar
    lateinit var songArt: ImageView
    lateinit var songTitle: TextView
    lateinit var artistName: TextView

    override fun onResume() {
        super.onResume()
        val musicPlayer = AeonMusicPlayer.getInstance(this)
        if(musicPlayer.isPlaying){
            playPause.setImageResource(R.drawable.pause)
        }
        else{
            playPause.setImageResource(R.drawable.play)
        }

        SongDialog.fragManager = supportFragmentManager
    }

    override fun onSongPlayed(context: Context, player: (AeonMusicPlayer) -> Unit) {
        playPause.setImageResource(R.drawable.pause)
        super.onSongPlayed(context){
            progressBar.max = it.duration
            songsViewModel.startMonitoringSongProgress(it)
            displaySong()
        }
    }

    override fun onSongPaused(context: Context, player: (AeonMusicPlayer) -> Unit) {
        playPause.setImageResource(R.drawable.play)
        super.onSongPaused(context) {}
    }

    fun displaySong() {
        onSongChanged {
            songTitle.text = it.title
            artistName.text = it.artist
            if(it.thumbnail != null)
                songArt.setImageBitmap(it.thumbnail)
            else songArt.setImageResource(R.drawable.aeon) // To put default art
        }
    }

    fun setUpListeners(){
        playPause.setOnClickListener{
            if (AeonMusicPlayer.getInstance(this).isPlaying)
                onSongPaused(this) {}
            else
                onSongPlayed(this) {}
        }

        skipNext.setOnClickListener{
            onSongSkippedToNext(this)
        }

        skipPrevious.setOnClickListener {
            onSongSkippedToPrevious(this)
        }

        AeonMusicPlayer.setPlaybackListener(this)
    }

    fun updateSongProgress(){
        songsViewModel.updateProgress(this){
            progressBar.progress = it
        }
    }
}