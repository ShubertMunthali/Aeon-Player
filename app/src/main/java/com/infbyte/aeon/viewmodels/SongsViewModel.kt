package com.infbyte.aeon.viewmodels

import android.content.Context
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infbyte.aeon.models.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SongsViewModel: ViewModel() {

    private val songs = mutableListOf<Song>()

    fun getAllSongs(context: Context): List<Song>{
        val cursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            SONG_PROJECTION,
            null,
            null,
            null
        )

        when{

            cursor == null -> {}

            !cursor.moveToFirst() -> {}

            else -> {
                val idCOLUMN = cursor.getColumnIndex(MediaStore.Audio.AudioColumns._ID)
                val titleCOLUMN = cursor.getColumnIndex(MediaStore.MediaColumns.TITLE)
                val artistCOLUMN = cursor.getColumnIndex(MediaStore.MediaColumns.ARTIST)
                val albumCOLUMN = cursor.getColumnIndex(MediaStore.MediaColumns.ALBUM)
                val durationCOLUMN = cursor.getColumnIndex(MediaStore.MediaColumns.DURATION)

                if (songs.isNotEmpty())
                    songs.clear()

                do {
                    val song = Song(
                        cursor.getLong(idCOLUMN),
                        cursor.getString(titleCOLUMN),
                        cursor.getString(artistCOLUMN),
                        cursor.getString(albumCOLUMN),
                        cursor.getLong(durationCOLUMN)
                    )
                    songs.add(song)
                } while(cursor.moveToNext())
            }
        }

        return songs
    }

    fun getSongById(id: Long): Song{
        var song: Song? = null
                for(_song in songs){
                    if(_song.id == id)
                        song = _song
                }
                if (song == null)
                    song = songs[0]
        return song
    }

    companion object{
        val SONG_PROJECTION = arrayOf(
            MediaStore.Audio.AudioColumns._ID,
            MediaStore.Audio.AudioColumns.TITLE,
            MediaStore.MediaColumns.ARTIST,
            MediaStore.Audio.AlbumColumns.ALBUM,
            MediaStore.Audio.AudioColumns.DURATION
            //MediaStore.Audio.AudioColumns.TRACK,
            //MediaStore.Audio.AudioColumns.YEAR,
            //MediaStore.Audio.ArtistColumns.ARTIST,
            //MediaStore.Audio.Media.DATA,
            //MediaStore.Audio.AudioColumns.ARTIST_ID
        )
    }
}