package com.infbyte.aeon.ui.fragments.dialogs

import android.app.Dialog
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.infbyte.aeon.R
import com.infbyte.aeon.databinding.SongDialogBinding
import com.infbyte.aeon.models.Song
import com.infbyte.aeon.ui.adapters.OptionsAdapter

class SongDialog: DialogFragment() {

    private val container: ViewGroup? = null
    private var _binding: SongDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = SongDialogBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())
            .setView(binding.root)
        val items = listOf(
            getString(PLAY_NOW),
            getString(PLAY_NEXT),
            getString(ADD_TO_PLAYLIST),
            getString(FAVORITE),
            getString(SONG_INFO)
        )
        val recyclerView: RecyclerView = binding.include.recyclerView
        val layoutManager = LinearLayoutManager(requireContext())
        val dialog = builder.create()
        val adapter = OptionsAdapter(dialog, items, selectedSong, songs)

        recyclerView.apply{
            this.adapter = adapter
            this.layoutManager = layoutManager
        }
        SongInfo.fragManager = parentFragmentManager
        SongInfo.selectedSong = selectedSong
        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun show(){
         show(fragManager, TAG)
    }

    companion object{
        @StringRes private const val PLAY_NOW = R.string.play_now
        @StringRes private const val PLAY_NEXT = R.string.play_next
        @StringRes private const val FAVORITE = R.string.favorite
        @StringRes private const val ADD_TO_PLAYLIST = R.string.add_to_playlist
        @StringRes private const val SONG_INFO = R.string.song_info
        private const val TAG = "Song dialog"
        lateinit var fragManager: FragmentManager
        lateinit var songs: List<Song>
        lateinit var selectedSong: Song

        fun newInstance() = SongDialog()
    }
}