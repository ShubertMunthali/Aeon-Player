package com.infbyte.aeon.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.findFragment
import androidx.recyclerview.widget.RecyclerView
import com.infbyte.aeon.R
import com.infbyte.aeon.models.Song
import com.infbyte.aeon.playback.AeonMusicPlayer
import com.infbyte.aeon.ui.fragments.dialogs.SongDialog
import de.hdodenhof.circleimageview.CircleImageView

class SongsAdapter(private val songs: List<Song>): RecyclerView.Adapter<SongsAdapter.SongViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.song, parent, false)
        return SongViewHolder(itemView)
    }

    override fun getItemCount() = songs.size

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.title.text = songs[position].title
        holder.artist.text = songs[position].artist

        holder.moreOptions.setOnClickListener(holder.getMoreOptionsListener(position))
        holder.songView.setOnClickListener(holder.getSongSelectedListener(position))
    }

    inner class SongViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val title: TextView = itemView.findViewById(R.id.songTitle)
        val artist: TextView = itemView.findViewById(R.id.artistName)
        val songView: View = itemView.findViewById(R.id.songView)
        val moreOptions: View = itemView.findViewById(R.id.moreOptions)

        fun getMoreOptionsListener(position: Int) = View.OnClickListener{
            SongDialog.songs = songs
            SongDialog.selectedSong = songs[position]
            val songDialog = SongDialog.newInstance()
            songDialog.show()
        }

        fun getSongSelectedListener(position: Int) = View.OnClickListener{
            val context = it.context
            if(songs.isNotEmpty()) {
                AeonMusicPlayer.setNextSong(null)
                val selectedSong = songs[position]
                if(selectedSong != AeonMusicPlayer.getCurrentSong())
                    AeonMusicPlayer.getInstance(context).apply {
                        prepareSong(selectedSong)
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
    }
}