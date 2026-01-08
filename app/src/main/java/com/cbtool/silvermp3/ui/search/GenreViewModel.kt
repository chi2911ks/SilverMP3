package com.cbtool.silvermp3.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cbtool.silvermp3.data.model.Song
import com.cbtool.silvermp3.interfaces.SongRepository
import kotlinx.coroutines.launch

class GenreViewModel(private val songRepository: SongRepository): ViewModel() {
    private val _songs = MutableLiveData<List<Song>>()
    val songs: LiveData<List<Song>> = _songs
    fun getSongByGenre(genre: String){
        viewModelScope.launch {
            _songs.value = songRepository.getSongByGenre(genre)
        }
    }
}