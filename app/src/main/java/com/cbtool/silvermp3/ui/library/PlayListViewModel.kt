package com.cbtool.silvermp3.ui.library

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cbtool.silvermp3.data.model.Song
import com.cbtool.silvermp3.data.repository.firestore.UserPlaylistRepository
import kotlinx.coroutines.launch

class PlayListViewModel(private val playlistRepository: UserPlaylistRepository): ViewModel() {
    private val _song = MutableLiveData<List<Song>>()
    val song: LiveData<List<Song>> = _song
    fun getSongs(playlistId: String){
        viewModelScope.launch {
            val songs = playlistRepository.getSongs(playlistId)
            _song.value = songs
        }
    }
}