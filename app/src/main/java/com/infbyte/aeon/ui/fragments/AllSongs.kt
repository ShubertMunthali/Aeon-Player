package com.infbyte.aeon.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.widget.PopupMenuCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.infbyte.aeon.R
import com.infbyte.aeon.databinding.AllSongsBinding
import com.infbyte.aeon.playback.AeonMusicPlayer
import com.infbyte.aeon.ui.adapters.SongsAdapter
import com.infbyte.aeon.ui.fragments.dialogs.SongDialog
import com.infbyte.aeon.viewmodels.AeonMusicViewModel

class AllSongs: Fragment() {

    private var _binding: AllSongsBinding? = null
    private val binding get() = _binding!!
    private val songsViewModel: AeonMusicViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AllSongsBinding.inflate(layoutInflater)
        SongDialog.fragManager = parentFragmentManager
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(requireContext())
        val adapter = SongsAdapter(songsViewModel.getAllSongs())
        val recyclerView = binding.container.recyclerView
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