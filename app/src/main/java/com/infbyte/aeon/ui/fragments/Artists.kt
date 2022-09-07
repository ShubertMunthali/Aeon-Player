package com.infbyte.aeon.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.infbyte.aeon.databinding.ArtistsBinding
import com.infbyte.aeon.models.Artist
import com.infbyte.aeon.ui.adapters.ArtistsAdapter
import com.infbyte.aeon.viewmodels.AeonMusicViewModel

class Artists: Fragment() {

    private var _binding: ArtistsBinding? = null
    private val binding get() = _binding!!
    private val songsViewModel: AeonMusicViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ArtistsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(requireContext())
        val adapter = ArtistsAdapter(songsViewModel.getAllArtists())
        val recyclerView = binding.include.recyclerView

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
        lateinit var selectedArtist: Artist
        fun newInstance() = Artists()
    }
}