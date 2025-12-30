package com.cbtool.silvermp3.ui.library

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cbtool.silvermp3.data.model.Playlist
import com.cbtool.silvermp3.data.model.Song
import com.cbtool.silvermp3.interfaces.UserPlaylistRepository
import kotlinx.coroutines.launch

class UserPlaylistViewModel(private val playlistRepository: UserPlaylistRepository) : ViewModel() {
    private val _song = MutableLiveData<List<Song>>()
    val song: LiveData<List<Song>> = _song
    private val _playlist = MutableLiveData<Playlist>()
    val playlist: LiveData<Playlist> = _playlist
    fun getSongs(playlistId: String) {
        viewModelScope.launch {
            val songs = playlistRepository.getSongs(playlistId)
            _song.value = songs
        }
    }

    fun getPlaylist(playlistId: String) {
        viewModelScope.launch {
            val playlists = playlistRepository.getPlaylist(playlistId)
            _playlist.value = playlists
        }
    }

    fun deletePlaylist(playlistId: String) {
        playlistRepository.remove(playlistId)
    }

    fun addPlaylist(name: String) {
        playlistRepository.create(name)
    }

    fun updatePlaylist(playlistId: String, name: String, desc: String) {
        playlistRepository.update(playlistId, name, desc)
    }
}