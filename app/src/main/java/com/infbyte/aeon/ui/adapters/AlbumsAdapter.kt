package com.infbyte.aeon.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.infbyte.aeon.R
import com.infbyte.aeon.models.Album
import com.infbyte.aeon.ui.activities.SongsActivity
import com.infbyte.aeon.ui.fragments.Albums
import com.infbyte.aeon.ui.fragments.Songs
import de.hdodenhof.circleimageview.CircleImageView

class AlbumsAdapter(private val albums: List<Album>): RecyclerView.Adapter<AlbumsAdapter.AlbumViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.album, parent, false)
        return AlbumViewHolder(itemView)
    }

    override fun getItemCount() = albums.size

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.albumTitle.text = albums[position].title
        holder.numberOfSongs.text = albums[position].numberOfSongs.toString()
        val art = albums[position].art
        if(art != null)
            holder.albumArt.setImageBitmap(art)
        else holder.albumArt.setImageResource(R.drawable.aeon)

        holder.itemView.setOnClickListener(holder.getOnAlbumSelectedListener(position))
    }

    inner class AlbumViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val albumTitle: TextView = itemView.findViewById(R.id.albumTitle)
        val numberOfSongs: TextView = itemView.findViewById(R.id.numberOfSongs)
        val albumArt: CircleImageView = itemView.findViewById(R.id.albumArt)

        fun getOnAlbumSelectedListener(position: Int) = View.OnClickListener {
            Albums.selectedAlbum = albums[position]
            Songs.from = Songs.ALBUMS
            SongsActivity.getInstance().start(it.context)
        }
    }
}