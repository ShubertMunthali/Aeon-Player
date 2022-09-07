package com.infbyte.aeon.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.infbyte.aeon.R
import com.infbyte.aeon.models.Folder
import com.infbyte.aeon.ui.activities.SongsActivity
import com.infbyte.aeon.ui.fragments.Folders
import com.infbyte.aeon.ui.fragments.Songs

class FoldersAdapter(private val folders: List<Folder>): RecyclerView.Adapter<FoldersAdapter.FolderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.folder, parent, false)
        return FolderViewHolder(itemView)
    }

    override fun getItemCount() = folders.size

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        holder.folderName.text = folders[position].name
        holder.numberOfSongs.text = folders[position].numberOfSongs.toString()

        holder.itemView.setOnClickListener(holder.getOnFolderSelectedListener(position))
    }

    inner class FolderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val folderName: TextView = itemView.findViewById(R.id.folderName)
        val numberOfSongs: TextView = itemView.findViewById(R.id.numberOfSongs)

        fun getOnFolderSelectedListener(position: Int) = View.OnClickListener {
            Folders.selectedFolder = folders[position]
            Songs.from = Songs.FOLDERS
            SongsActivity
                .getInstance()
                .start(it.context)
        }
    }
}