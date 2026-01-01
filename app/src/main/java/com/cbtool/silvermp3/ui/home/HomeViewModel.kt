package com.cbtool.silvermp3.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cbtool.silvermp3.data.model.Artist
import com.cbtool.silvermp3.data.model.Playlist
import com.cbtool.silvermp3.data.model.Song
import com.cbtool.silvermp3.interfaces.ArtistsRepository
import com.cbtool.silvermp3.interfaces.PlaylistRepository
import com.cbtool.silvermp3.interfaces.SongRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val songsRepository: SongRepository,
    private val playlistRepository: PlaylistRepository,
    private val artistsRepository: ArtistsRepository
) : ViewModel() {
    private val _songs = MutableLiveData<List<Song>>()
    val songs: LiveData<List<Song>> = _songs
    private val _playlists = MutableLiveData<List<Playlist>>()
    val playlists: LiveData<List<Playlist>> = _playlists
    private val _artists = MutableLiveData<List<Artist>>()
    val artists: LiveData<List<Artist>> = _artists

    fun loadSongs() {
        viewModelScope.launch {
            _songs.value = songsRepository.getSongSuggest(5)
        }
    }

    fun loadPlaylists() {
        viewModelScope.launch {
            _playlists.value = playlistRepository.getPlaylists()
        }

    }
    fun loadArtists() {
        viewModelScope.launch {
            _artists.value = artistsRepository.getPopularArtists()
        }

    }
}