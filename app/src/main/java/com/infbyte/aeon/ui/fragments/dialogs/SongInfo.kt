package com.infbyte.aeon.ui.fragments.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.infbyte.aeon.databinding.SongInfoBinding
import com.infbyte.aeon.models.Song

class SongInfo: DialogFragment() {

    private var _binding: SongInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = SongInfoBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())
            .setView(binding.root)
        val thumbnail = selectedSong.thumbnail
        val albumArt = binding.albumArt
        if(thumbnail != null)
            albumArt.setImageBitmap(selectedSong.thumbnail)
        binding.songTitle.text = selectedSong.title
        binding.albumTitle.text = selectedSong.album
        binding.artistName.text = selectedSong.artist
        binding.path.text = selectedSong.path
        binding.size.text = selectedSong.size.toString()
        binding.bitRate.text = selectedSong.bitrate.toString()
        return builder.create()
    }

    fun show(){
        show(fragManager, TAG)
    }


    companion object{
        private const val TAG = "Song info"
        lateinit var fragManager: FragmentManager
        lateinit var selectedSong: Song

        fun newInstance() = SongInfo()
    }
}