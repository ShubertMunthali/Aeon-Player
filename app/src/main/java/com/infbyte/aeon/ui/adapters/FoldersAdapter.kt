package com.infbyte.aeon.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.infbyte.aeon.R
import com.infbyte.aeon.databinding.FolderBinding
import com.infbyte.aeon.models.Folder

class FoldersAdapter(private val folders: List<Folder>): RecyclerView.Adapter<FoldersAdapter.FolderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.folder, parent, false)
        return FolderViewHolder(itemView)
    }

    override fun getItemCount() = folders.size

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        holder.folderName.text = folders[position].name
        holder.numberOfSongs.text = folders[position].numberOfSongs.toString()
    }

    class FolderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val folderName: TextView = itemView.findViewById(R.id.folderName)
        val numberOfSongs: TextView = itemView.findViewById(R.id.numberOfSongs)
    }
}