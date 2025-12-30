package com.cbtool.silvermp3.ui.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cbtool.silvermp3.data.model.Playlist
import com.cbtool.silvermp3.data.model.Song
import com.cbtool.silvermp3.interfaces.PlaylistRepository
import kotlinx.coroutines.launch

class PlaylistViewModel(private val playlistRepository: PlaylistRepository) : ViewModel() {
    private val _playlist = MutableLiveData<Playlist>()
    val playlist: LiveData<Playlist> = _playlist
    private val _song = MutableLiveData<List<Song>>()
    val song: LiveData<List<Song>> = _song
    fun refresh(playlistId: String) {
        getDetailPlaylist(playlistId)
        getSongs(playlistId)
    }

    fun getDetailPlaylist(playlistId: String) {
        viewModelScope.launch {
            _playlist.value = playlistRepository.getDetailPlaylist(playlistId)
        }
    }

    fun getSongs(playlistId: String) {
        viewModelScope.launch {
            _song.value = playlistRepository.getSongs(playlistId)
        }
    }

}