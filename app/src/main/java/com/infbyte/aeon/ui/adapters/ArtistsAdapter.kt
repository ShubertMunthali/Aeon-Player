package com.infbyte.aeon.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.infbyte.aeon.R
import com.infbyte.aeon.models.Artist
import com.infbyte.aeon.ui.activities.SongsActivity
import com.infbyte.aeon.ui.fragments.Artists
import com.infbyte.aeon.ui.fragments.Songs
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
        val art = artists[position].art
        if(art != null)
            holder.artistArt.setImageBitmap(art)
        else holder.artistArt.setImageResource(R.drawable.aeon)

        holder.itemView.setOnClickListener(holder.getOnArtistSelectedListener(position))
    }

    inner class ArtistViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val artistName: TextView = itemView.findViewById(R.id.artistName)
        val numberOfSongs: TextView = itemView.findViewById(R.id.numberOfSongs)
        val artistArt: CircleImageView = itemView.findViewById(R.id.artistArt)

        fun getOnArtistSelectedListener(position: Int) = View.OnClickListener{
            Artists.selectedArtist = artists[position]
            Songs.from = Songs.ARTISTS
            SongsActivity.getInstance().start(it.context)
        }
    }
}