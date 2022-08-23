package com.infbyte.aeon.ui.activities

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.infbyte.aeon.BuildConfig
import com.infbyte.aeon.R
import com.infbyte.aeon.contracts.AeonResultContracts
import com.infbyte.aeon.databinding.AeonMainBinding
import com.infbyte.aeon.permissions.AeonPermissions
import com.infbyte.aeon.playback.AeonMusicPlayer
import com.infbyte.aeon.preferences.AeonPreferences
import com.infbyte.aeon.ui.adapters.PagerAdapter
import com.infbyte.aeon.ui.fragments.*
import com.infbyte.aeon.ui.listeners.PlaybackListener
import com.infbyte.aeon.viewmodels.AeonMusicViewModel
import java.util.concurrent.Executors

class AeonMainActivity: AppCompatActivity(), PlaybackListener {

    private var _binding: AeonMainBinding? = null
    private val binding get() = _binding!!

    private val allSongs = AllSongs.newInstance()
    private val playlists = Playlists.newInstance()
    private val artists = Artists.newInstance()
    private val albums = Albums.newInstance()
    private val folders = Folders.newInstance()

    private val songsViewModel: AeonMusicViewModel by viewModels()

    private val fragments =
        listOf(
            allSongs,
            playlists,
            artists,
            albums,
            folders
        )

    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ isGranted ->
        if(isGranted) {
            loadMedia()
            initMusicPlayer()
        }
        else finish()
    }

    private val permissionLauncherAPI30 = registerForActivityResult(AeonResultContracts.API30RequestPermission()){ isGranted ->
        if(isGranted) {
            loadMedia()
            initMusicPlayer()
        }
        else finish()
    }

    private lateinit var playPause: ImageView
    private lateinit var skipNext: ImageView
    private lateinit var skipPrevious: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var songArt: ImageView
    private lateinit var songTitle: TextView
    private lateinit var artistName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = AeonMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        playPause = binding.nowPlaying.playPause
        skipNext = binding.nowPlaying.next
        skipPrevious = binding.nowPlaying.previous
        progressBar = binding.nowPlaying.progressBar
        songArt = binding.nowPlaying.songArt
        songTitle = binding.nowPlaying.songTitle
        artistName = binding.nowPlaying.artistName

        if(!AeonPermissions.isReadPermissionGranted(this))
            if(Build.VERSION.SDK_INT == Build.VERSION_CODES.R) {
                val packageUri = "package:${BuildConfig.APPLICATION_ID}"
                permissionLauncherAPI30.launch(packageUri)
            }
            else
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        else {
            loadMedia()
            initMusicPlayer()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        AeonMusicPlayer.getInstance(this).release()
    }

    //Playback Methods

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

    private fun loadMedia(){

        val pagerAdapter = PagerAdapter(fragments, this)
        val tab = binding.tab
        val viewPager2 = binding.viewPager2

        viewPager2.adapter = pagerAdapter

        //Connecting the ViewPager2 and the TabLayout for swipe functionality
        TabLayoutMediator(tab, viewPager2, true, true) { tabItem, position ->
            tabItem.text = getString(PagerAdapter.TAB_TITLES[position])
        }.attach()

        songsViewModel.loadAllSongs(this)
    }

    private fun displaySong() {
        onSongChanged {
            songTitle.text = it.title
            artistName.text = it.artist
            if(it.thumbnail != null)
                songArt.setImageBitmap(it.thumbnail)
            else songArt.setImageBitmap(null) // To put default art
        }
    }

    private fun initMusicPlayer(){

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

        val preferences = AeonPreferences.getInstance(this)

        AeonMusicPlayer.setPlaybackListener(this)
        AeonMusicPlayer.setPlayMode(preferences.getPlayMode())
        AeonMusicPlayer.initialize(
            this, songsViewModel.getSongById(preferences.getCurrentSongId()))

        displaySong()

        songsViewModel.updateProgress(this){
            progressBar.progress = it
        }
    }
}
