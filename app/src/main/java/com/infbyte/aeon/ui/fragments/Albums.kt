package com.infbyte.aeon.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.infbyte.aeon.databinding.AlbumsBinding
import com.infbyte.aeon.ui.adapters.AlbumsAdapter
import com.infbyte.aeon.viewmodels.AlbumsViewModel

class Albums: Fragment() {

    private var _binding: AlbumsBinding? = null
    private val binding get() = _binding!!

    private val albumsViewModel: AlbumsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AlbumsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(requireContext())
        val adapter = AlbumsAdapter(albumsViewModel.getAllAlbums())
        val recyclerView = binding.container.recyclerView

        println(albumsViewModel.getAllAlbums().size)

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
        fun newInstance() = Albums()
    }
}