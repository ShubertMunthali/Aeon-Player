package com.infbyte.aeon.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.infbyte.aeon.R
import com.infbyte.aeon.databinding.ArtistBinding
import com.infbyte.aeon.models.Artist
import de.hdodenhof.circleimageview.CircleImageView

class ArtistsAdapter(private val artists: List<Artist>): RecyclerView.Adapter<ArtistsAdapter.ArtistViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.artist, parent, false)
        return ArtistViewHolder(itemView)
    }

    override fun getItemCount() = artists.size

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        holder.artistName.text = artists[position].name
        holder.numberOfSongs.text = artists[position].numberOfSongs.toString()
        holder.artistArt.setImageBitmap(artists[position].art)
    }

    class ArtistViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val artistName: TextView = itemView.findViewById(R.id.artistName)
        val numberOfSongs: TextView = itemView.findViewById(R.id.numberOfSongs)
        val artistArt: CircleImageView = itemView.findViewById(R.id.artistArt)
    }
}