package com.infbyte.aeon.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
    }

    inner class SongViewHolder(itemView: View): RecyclerView.ViewHolder(itemView),
        View.OnClickListener{
        val title: TextView = itemView.findViewById(R.id.songTitle)
        val artist: TextView = itemView.findViewById(R.id.artistName)
        private val songView: View = itemView.findViewById(R.id.songView)
        private val moreOptions: View = itemView.findViewById(R.id.moreOptions)

        init{
            songView.setOnClickListener(this)
            moreOptions.setOnClickListener {
                SongDialog.songs = songs
                SongDialog.selectedSong = songs[adapterPosition]
                val songDialog = SongDialog.newInstance()
                songDialog.show()
            }
        }

        override fun onClick(v: View) {
            val context = v.context
            if(songs.isNotEmpty()) {
                val selectedSong = songs[adapterPosition]
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