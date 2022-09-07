package com.infbyte.aeon.ui.adapters

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.infbyte.aeon.R
import com.infbyte.aeon.models.Song
import com.infbyte.aeon.playback.AeonMusicPlayer
import com.infbyte.aeon.ui.fragments.dialogs.SongInfo

class OptionsAdapter(
    private val owner: Dialog,
    private val items: List<String>,
    private val song: Song,
    private val songs: List<Song>):
    RecyclerView.Adapter<OptionsAdapter.OptionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.option, parent, false)
        return OptionViewHolder(itemView)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        holder.name.text = items[position]
    }

    inner class OptionViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{

        val name: TextView = itemView.findViewById(R.id.option)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val context = v.context
            when(adapterPosition){
                0 -> playNow(context)
                1 -> queueForNextPlay(context)
                2 -> {}
                3 -> {}
                else -> {showSongInfo()}
            }
            owner.dismiss()
        }

        private fun playNow(context: Context){
            if(songs.isNotEmpty()) {
                AeonMusicPlayer.setNextSong(null)
                if(song != AeonMusicPlayer.getCurrentSong())
                    AeonMusicPlayer.getInstance(context).apply {
                        prepareSong(song)
                        setCurrentSongs(songs)
                        play()
                    }

                if(!AeonMusicPlayer.getInstance(context).isPlaying)
                    AeonMusicPlayer.getInstance(context).apply{
                        setCurrentSongs(songs)
                        play()
                    }
                else{
                    AeonMusicPlayer.getInstance(context).apply {
                        setCurrentSongs(songs)
                    }
                    //Open playing activity
                }
            }
        }

        private fun queueForNextPlay(context: Context){
            AeonMusicPlayer.setNextSong(song)
            AeonMusicPlayer.getInstance(context).setCurrentSongs(songs)
        }

        private fun showSongInfo(){
            SongInfo.newInstance().show()
        }
    }
}