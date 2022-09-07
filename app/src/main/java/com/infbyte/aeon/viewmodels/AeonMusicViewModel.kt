package com.infbyte.aeon.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infbyte.aeon.models.Album
import com.infbyte.aeon.models.Artist
import com.infbyte.aeon.models.Folder
import com.infbyte.aeon.models.Song
import com.infbyte.aeon.playback.AeonMusicPlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AeonMusicViewModel: ViewModel() {

    private val  defaultDispatcher = Dispatchers.Default
    private val albums = mutableListOf<Album>()
    private val artists = mutableListOf<Artist>()
    private val folders = mutableListOf<Folder>()

    private val progress = MutableLiveData<Int>()

    fun getAllSongs() = songs

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
                val artistIdCOLUMN = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST_ID)
                val albumCOLUMN = cursor.getColumnIndex(MediaStore.MediaColumns.ALBUM)
                val durationCOLUMN = cursor.getColumnIndex(MediaStore.MediaColumns.DURATION)
                val albumIdCOLUMN = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM_ID)
                val pathCOLUMN = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA)

                if (songs.isNotEmpty())
                    songs.clear()

                do {
                    val albumId = cursor.getLong(albumIdCOLUMN)
                    val uri = loadThumbnail(context, albumId)
                    val thumbnail = decodeImage(uri)
                    val path = cursor.getString(pathCOLUMN)

                    val song = Song(
                        cursor.getLong(idCOLUMN),
                        cursor.getString(titleCOLUMN),
                        if(cursor.getString(artistCOLUMN) == "<unknown>")
                            "Unknown artist"
                        else cursor.getString(artistCOLUMN),
                        cursor.getLong(artistIdCOLUMN),
                        cursor.getString(albumCOLUMN),
                        albumId,
                        cursor.getLong(durationCOLUMN),
                        uri,
                        getFolderName(path),
                        path,
                        thumbnail = thumbnail
                    )
                    songs.add(song)
                } while(cursor.moveToNext())
            }
        }
        return songs
    }

    fun getAllAlbums(): List<Album>{
        val albumNames = mutableListOf<String>()
        if(albums.isNotEmpty())
            albums.clear()
        if(songs.isNotEmpty()) {
            for (song in songs) {
                if(!albumNames.contains(song.album)) {
                    albumNames.add(song.album)
                    val album =
                        Album(song.albumId, song.album, song.thumbnail, countSongsInAlbum(song.albumId))
                    albums.add(album)
                }
            }
        }

        return albums
    }

    fun getSongsByAlbum(albumId: Long): List<Song>{
        val aSongs = mutableListOf<Song>()
        for(song in songs){
            if(song.albumId == albumId)
                aSongs.add(song)
        }
        return aSongs
    }

    private fun countSongsInAlbum(albumId: Long):Int{
        var numberOfSongs = 0
        for(song in songs){
            if(song.albumId == albumId)
                numberOfSongs++
        }
        return numberOfSongs
    }

    fun getAllArtists(): List<Artist>{
        val artistNames = mutableListOf<String>()
        if(artists.isNotEmpty())
            artists.clear()
        for(song in songs){
            if(!artistNames.contains(song.artist)) {
                artistNames.add(song.artist)
                val artist = Artist(
                    song.artistId,
                    song.artist,
                    countSongsByArtist(song.artistId),
                    song.thumbnail
                )
                artists.add(artist)
            }
        }
        return artists
    }

    fun getSongsByArtist(artistId: Long): List<Song>{
        val aSongs = mutableListOf<Song>()
        for(song in songs){
            if(song.artistId == artistId)
                aSongs.add(song)
        }
        return aSongs
    }

    private fun countSongsByArtist(artistId: Long): Int{
        var numberOfSongs = 0
        for(song in songs){
            if(song.artistId == artistId)
                numberOfSongs++
        }
        return numberOfSongs
    }

    fun getAllFolders(): List<Folder>{
        val folderNames = mutableListOf<String>()
        for(song in songs){
            if(!folderNames.contains(song.folder)){
                folderNames.add(song.folder)
                val folder = Folder(song.folder, countSongsInFolder(song.folder))
                folders.add(folder)
            }
        }
        return folders
    }

    fun getSongsByFolder(folderName: String): List<Song>{
        val fSongs = mutableListOf<Song>()
        for(song in songs){
            if(song.folder == folderName)
                fSongs.add(song)
        }
        return fSongs
    }

    private fun countSongsInFolder(name: String): Int{
        var numberOfSongs = 0
        for(song in songs)
            if(song.folder == name)
                numberOfSongs++
        return numberOfSongs
    }

    private fun getFolderName(path: String): String{
        val delimiter = "/"
        return path.substringBeforeLast(delimiter).substringAfterLast(delimiter)
    }

    private fun loadThumbnail(context: Context, albumId: Long): String?{
        var path: String? = null
        val selection = "${MediaStore.Audio.Albums._ID} = ?"
        val selectionArgs = arrayOf("$albumId")
        val cursor = context.contentResolver.query(
            MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, ALBUM_PROJECTION,
        selection, selectionArgs, null)
        when {
            cursor == null -> {
                println("Albums not returned")
            }
            cursor.moveToFirst() -> {
                val albumArtCOLUMN = cursor.getColumnIndex(MediaStore.Audio.Artists.Albums.ALBUM_ART)
                path = cursor.getString(albumArtCOLUMN)
            }
        }
        return path
    }

    private fun decodeImage(uri: String?): Bitmap? {
        var thumbnail: Bitmap? = null
        try {
            if(uri != null)
                thumbnail = BitmapFactory.decodeFile(uri)
        }
        catch(e: Exception){
            e.printStackTrace()
        }
        return thumbnail
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

    fun updateProgress(owner: LifecycleOwner, getProgress: (Int) -> Unit){
        progress.observe(owner) {
            getProgress(it)
        }
    }

    companion object{
        private val songs = mutableListOf<Song>()
        val SONG_PROJECTION = arrayOf(
            MediaStore.Audio.AudioColumns._ID,
            MediaStore.Audio.AudioColumns.TITLE,
            MediaStore.MediaColumns.ARTIST,
            MediaStore.Audio.AudioColumns.ARTIST_ID,
            MediaStore.Audio.AlbumColumns.ALBUM,
            MediaStore.Audio.AudioColumns.ALBUM_ID,
            MediaStore.Audio.AudioColumns.DURATION,
            MediaStore.Audio.AudioColumns.DATA
            //MediaStore.Audio.AudioColumns.TRACK,
            //MediaStore.Audio.AudioColumns.YEAR,
            //MediaStore.Audio.ArtistColumns.ARTIST,
            //MediaStore.Audio.Media.DATA,
        )

        val ALBUM_PROJECTION = arrayOf(
            MediaStore.Audio.Artists.Albums._ID,
            MediaStore.Audio.Artists.Albums.ALBUM_ART)
    }
}