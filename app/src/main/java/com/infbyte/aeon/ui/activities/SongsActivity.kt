package com.infbyte.aeon.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.infbyte.aeon.R
import com.infbyte.aeon.databinding.SongsActivityBinding
import com.infbyte.aeon.playback.AeonMusicPlayer
import com.infbyte.aeon.ui.fragments.Songs

class SongsActivity: MainActivity() {

    private var _binding: SongsActivityBinding? = null
    private val binding get() = _binding!!
    private val songs = Songs.newInstance()


    fun start(context: Context){
        val intent = Intent(context, SongsActivity::class.java)
        context.startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            _binding = SongsActivityBinding.inflate(layoutInflater)
            setContentView(binding.root)
            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            playPause = binding.nowPlaying.playPause
            skipNext = binding.nowPlaying.next
            skipPrevious = binding.nowPlaying.previous
            progressBar = binding.nowPlaying.progressBar
            songArt = binding.nowPlaying.songArt
            songTitle = binding.nowPlaying.songTitle
            artistName = binding.nowPlaying.artistName
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, songs, Songs.TAG)
                .commit()

            setUpListeners()
            displaySong()
            updateSongProgress()
        }
    }

    override fun onStart() {
        super.onStart()
        binding.toolbar.setNavigationOnClickListener{
            onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object{

        private var songsActivity: SongsActivity? = null

        fun getInstance(): SongsActivity{
            if(songsActivity == null)
                songsActivity = SongsActivity()
            return songsActivity!!
        }
    }
}