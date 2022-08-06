package com.infbyte.aeon.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.infbyte.aeon.R
import com.infbyte.aeon.models.Song
import com.infbyte.aeon.playback.AeonMusicPlayer

class SongsAdapter(private val songs: List<Song>): RecyclerView.Adapter<SongsAdapter.SongViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.song, parent, false)
        return SongViewHolder(itemView)
    }

    override fun getItemCount() = songs.size

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.title.text = songs[position].title
        holder.artist.text = songs[position].artist
    }


    inner class SongViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val title: TextView = itemView.findViewById(R.id.songTitle)
        val artist: TextView = itemView.findViewById(R.id.artistName)

        init{
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val context = v.context
            if(songs.isNotEmpty()) {
                val selectedSong = songs[adapterPosition]
                AeonMusicPlayer.getInstance(context, selectedSong).apply {
                    setCurrentSongs(songs)
                    play()
                }
            }
        }
    }

    companion object{
        var aeonMusicPlayer: AeonMusicPlayer? = null
    }
}