package com.cbtool.silvermp3.ui.library

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cbtool.silvermp3.data.model.LibraryItem
import com.cbtool.silvermp3.data.model.Playlist
import com.cbtool.silvermp3.data.model.Song
import com.cbtool.silvermp3.data.repository.firestore.UserFavouriteRepository
import com.cbtool.silvermp3.data.repository.firestore.UserPlaylistRepository
import kotlinx.coroutines.launch

class LibraryViewModel(
    private val userPlaylistRepository: UserPlaylistRepository,
    private val userFavouriteRepository: UserFavouriteRepository
): ViewModel() {
    private val _libItems = MutableLiveData<List<LibraryItem>>()
    val libItems: LiveData<List<LibraryItem>> = _libItems

    fun getPlaylists(){
        val items = mutableListOf<LibraryItem>()
        viewModelScope.launch {
            val count = userFavouriteRepository.getCount()
            val playlists = userPlaylistRepository.getPlaylists()
            items.add(LibraryItem.FavouriteItem(count))
            items.addAll(playlists.map { LibraryItem.PlaylistItem(it) })
            _libItems.value = items
        }
    }
    suspend fun containsPlaylist(songId: String): List<String> {
        val playlistIds = mutableListOf<String>()
        if (userFavouriteRepository.isFavourite(songId)){
            playlistIds.add("favourites")
        }
        playlistIds.addAll(userPlaylistRepository.getPlaylistsContainingSong(songId))
        return playlistIds
    }

    fun addSongToPlaylist(playlistId: String, song: Song){
        userPlaylistRepository.addSong(playlistId, song)
    }
    fun removeSongFromPlaylist(playlistId: String, songId: String){
        userPlaylistRepository.removeSong(playlistId, songId)
    }
    fun addSongToFavourite(song: Song){
        userFavouriteRepository.addSong(song)
    }
    fun removeSongFromFavourite(songId: String){
        userFavouriteRepository.removeSong(songId)
    }


}