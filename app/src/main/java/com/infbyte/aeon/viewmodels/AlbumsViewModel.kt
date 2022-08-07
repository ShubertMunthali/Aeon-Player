package com.infbyte.aeon.viewmodels

import android.content.Context
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI
import androidx.lifecycle.ViewModel
import com.infbyte.aeon.models.Album
import com.infbyte.aeon.models.Song

class AlbumsViewModel: ViewModel() {

    private val albums = mutableListOf<Album>()

    fun getAllAlbums() = albums

    fun loadAllAlbums(context: Context, songs: List<Song>){

        val song = songs[3]
        val cursor = context.contentResolver.query(
            EXTERNAL_CONTENT_URI,
        PROJECTION, MediaStore.Audio.Albums._ID+ "=?", arrayOf(song.albumId.toString()), null)

        when{
            cursor == null -> {}
            cursor.moveToFirst() -> {
                println("No albums available")
            }
            else ->{
                //val idCOLUMN = cursor.getColumnIndex(MediaStore.Audio.Albums._ID)
              //  val albumCOLUMN = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM)
                //val artistCOLUMN = cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST)
                val artCOLUMN = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART)
                //val numSongsCOLUMN = cursor.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS)

                if(albums.isNotEmpty())
                    albums.clear()

                do {
                    val album = Album(
                        0,
                       //cursor.getLong(idCOLUMN),
                        //cursor.getString(albumCOLUMN),
                        //cursor.getString(artistCOLUMN),
                        cursor.getString(artCOLUMN),
                        //cursor.getInt(numSongsCOLUMN)
                    )
                    albums.add(album)
                    println(album.art)
                } while(cursor.moveToNext())
            }
        }
    }

    /*fun addMusicArtToSong(song: Song){
        for(album in albums){
            if(song.album == album.title)
                song.art = album.art
        }
    }*/

    fun printAlbums(){
        for(album in albums) {
            println(album.art)
            break
        }
    }

    companion object{
        val PROJECTION = arrayOf(
            //MediaStore.Audio.Albums._ID,
            //MediaStore.Audio.Albums.ALBUM,
            //MediaStore.Audio.Albums.ARTIST,
            MediaStore.Audio.Albums.ALBUM_ART,
            //MediaStore.Audio.Albums.NUMBER_OF_SONGS
        )
    }
}