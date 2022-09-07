package com.infbyte.aeon.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.infbyte.aeon.R
import com.infbyte.aeon.databinding.AllSongsBinding
import com.infbyte.aeon.ui.adapters.SongsAdapter
import com.infbyte.aeon.ui.fragments.dialogs.SongDialog
import com.infbyte.aeon.viewmodels.AeonMusicViewModel

class Songs : Fragment(){

    private var _binding: AllSongsBinding? = null
    private val binding get() = _binding!!
    private val aeonMusicViewModel: AeonMusicViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if(savedInstanceState == null) {
            _binding = AllSongsBinding.inflate(layoutInflater)
            val activity = requireActivity()
            when (from) {
                ARTISTS -> activity.title = Artists.selectedArtist.name
                ALBUMS -> activity.title = Albums.selectedAlbum.title
                else -> activity.title = Folders.selectedFolder.name
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(requireContext())
        val songs = when(from){
            ARTISTS ->
                aeonMusicViewModel.getSongsByArtist(Artists.selectedArtist.id)
            ALBUMS -> aeonMusicViewModel.getSongsByAlbum(Albums.selectedAlbum.id)
            else -> aeonMusicViewModel.getSongsByFolder(Folders.selectedFolder.name)
        }
        val adapter =  SongsAdapter(songs)
        binding.container.recyclerView.apply {
            this.layoutManager = layoutManager
            this.adapter = adapter
        }
    }

    companion object{

        const val TAG = "Songs"
        const val ARTISTS = R.string.artists
        const val ALBUMS = R.string.albums
        const val FOLDERS = R.string.folders
        var from = ARTISTS
        fun newInstance() = Songs()
    }
}