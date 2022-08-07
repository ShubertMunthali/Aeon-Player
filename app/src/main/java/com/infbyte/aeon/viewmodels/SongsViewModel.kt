package com.infbyte.aeon.viewmodels

import android.content.Context
import android.provider.MediaStore
import android.provider.MediaStore.Audio.AlbumColumns.ALBUM_ART
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infbyte.aeon.models.Album
import com.infbyte.aeon.models.Song
import com.infbyte.aeon.playback.AeonMusicPlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SongsViewModel: ViewModel() {

    private val  defaultDispatcher = Dispatchers.Default
    private val songs = mutableListOf<Song>()

    private val progress = MutableLiveData<Int>()

    fun getAllSongs() = songs

    fun onAddMusicArt(album: (Song) -> Unit) {
        for(song in songs){
            album(song)
        }
    }

    fun loadAllSongs(context: Context): List<Song>{
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
                val albumIdCOLUMN = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM_ID)
                val pathCOLUMN = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA)

                if (songs.isNotEmpty())
                    songs.clear()

                do {
                    val song = Song(
                        cursor.getLong(idCOLUMN),
                        cursor.getString(titleCOLUMN),
                        if(cursor.getString(artistCOLUMN) == "<unknown>")
                            "Unknown artist"
                        else cursor.getString(artistCOLUMN),
                        cursor.getString(albumCOLUMN),
                        cursor.getLong(durationCOLUMN),
                        cursor.getInt(albumIdCOLUMN),
                        path = cursor.getString(pathCOLUMN)
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

    fun startMonitoringSongProgress(player: AeonMusicPlayer){
        viewModelScope.launch {
            withContext(defaultDispatcher) {
                do {
                    progress.postValue(player.currentPosition)
                } while (player.isPlaying)
            }
        }
    }

    fun getProgress() = progress

    companion object{
        val SONG_PROJECTION = arrayOf(
            MediaStore.Audio.AudioColumns._ID,
            MediaStore.Audio.AudioColumns.TITLE,
            MediaStore.MediaColumns.ARTIST,
            MediaStore.Audio.AlbumColumns.ALBUM,
            MediaStore.Audio.AudioColumns.DURATION,
            MediaStore.Audio.AudioColumns.ALBUM_ID,
            MediaStore.Audio.AudioColumns.DATA,
            //MediaStore.Audio.AudioColumns.TRACK,
            //MediaStore.Audio.AudioColumns.YEAR,
            //MediaStore.Audio.ArtistColumns.ARTIST,
            //MediaStore.Audio.Media.DATA,
            //MediaStore.Audio.AudioColumns.ARTIST_ID
        )
    }
}