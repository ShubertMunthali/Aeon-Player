package com.infbyte.aeon.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.infbyte.aeon.R
import com.infbyte.aeon.models.Album
import de.hdodenhof.circleimageview.CircleImageView

class AlbumsAdapter(private val albums: List<Album>): RecyclerView.Adapter<AlbumsAdapter.AlbumViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.album, parent, false)
        return AlbumViewHolder(itemView)
    }

    override fun getItemCount() = albums.size

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.albumTitle.text = albums[position].art
        holder.numberOfSongs.text = albums[position].id.toString()
    }

    class AlbumViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val albumTitle: TextView = itemView.findViewById(R.id.albumTitle)
        val numberOfSongs: TextView = itemView.findViewById(R.id.numberOfSongs)
        val albumArt: CircleImageView = itemView.findViewById(R.id.albumArt)
    }
}