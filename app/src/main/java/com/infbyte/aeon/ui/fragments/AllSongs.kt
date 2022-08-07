package com.infbyte.aeon.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.infbyte.aeon.databinding.AllSongsBinding
import com.infbyte.aeon.ui.adapters.SongsAdapter
import com.infbyte.aeon.viewmodels.AlbumsViewModel
import com.infbyte.aeon.viewmodels.SongsViewModel

class AllSongs: Fragment() {

    private var _binding: AllSongsBinding? = null
    private val binding get() = _binding!!

    private val songsViewModel: SongsViewModel by activityViewModels()
    private val albumsViewModel: AlbumsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AllSongsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(requireContext())
        val adapter = SongsAdapter(songsViewModel.getAllSongs())

        val recyclerView = binding.container.recyclerView

        albumsViewModel.loadAllAlbums(requireContext(), songsViewModel.getAllSongs())
        /*songsViewModel.onAddMusicArt {
            albumsViewModel.addMusicArtToSong(it)
            println(it.art)
        }*/

        recyclerView.apply{
            this.adapter = adapter
            this.layoutManager = layoutManager
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object{
        fun newInstance() = AllSongs()
    }
}