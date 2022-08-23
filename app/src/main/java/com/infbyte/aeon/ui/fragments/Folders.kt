package com.infbyte.aeon.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.infbyte.aeon.databinding.FoldersBinding
import com.infbyte.aeon.ui.adapters.FoldersAdapter
import com.infbyte.aeon.viewmodels.AeonMusicViewModel

class Folders: Fragment() {

    private var _binding: FoldersBinding? = null
    private val binding get() = _binding!!

    private val songsViewModel: AeonMusicViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FoldersBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(requireContext())
        val adapter = FoldersAdapter(songsViewModel.getAllFolders())
        val recyclerView = binding.include.recyclerView

        recyclerView.apply{
            this.adapter = adapter
            this.layoutManager = layoutManager
        }
    }

    override fun onDestroyView(){
        super.onDestroyView()
        _binding = null
    }

    companion object{
        fun newInstance() = Folders()
    }
}