package com.infbyte.aeon.ui.activities

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.tabs.TabLayoutMediator
import com.infbyte.aeon.BuildConfig
import com.infbyte.aeon.contracts.AeonResultContracts
import com.infbyte.aeon.databinding.AeonMainBinding
import com.infbyte.aeon.permissions.AeonPermissions
import com.infbyte.aeon.playback.AeonMusicPlayer
import com.infbyte.aeon.preferences.AeonPreferences
import com.infbyte.aeon.ui.adapters.PagerAdapter
import com.infbyte.aeon.ui.fragments.*

class AeonMainActivity: MainActivity(){

    private var _binding: AeonMainBinding? = null
    private val binding get() = _binding!!

    private val allSongs = AllSongs.newInstance()
    private val playlists = Playlists.newInstance()
    private val artists = Artists.newInstance()
    private val albums = Albums.newInstance()
    private val folders = Folders.newInstance()

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState == null) {
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

            if (!AeonPermissions.isReadPermissionGranted(this))
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R) {
                    val packageUri = "package:${BuildConfig.APPLICATION_ID}"
                    permissionLauncherAPI30.launch(packageUri)
                } else
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            else {
                loadMedia()
                initMusicPlayer()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        //AeonMusicPlayer.getInstance(this).release()
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

    private fun initMusicPlayer(){
        setUpListeners()
        val preferences = AeonPreferences.getInstance(this)
        AeonMusicPlayer.setPlayMode(preferences.getPlayMode())
        AeonMusicPlayer.initialize(
            this, songsViewModel.getSongById(preferences.getCurrentSongId()))

        displaySong()

        updateSongProgress()
    }
}
