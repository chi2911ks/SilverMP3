package com.cbtool.silvermp3.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cbtool.silvermp3.data.model.Song
import com.cbtool.silvermp3.data.repository.firestore.SongsRepository
import com.cbtool.silvermp3.data.repository.firestore.UserFavouriteRepository

class HomeViewModel(private val songsRepository: SongsRepository): ViewModel() {
    private val _songs = MutableLiveData<List<Song>>()
    val songs: LiveData<List<Song>> = _songs
    fun loadSongs(){
        songsRepository.getSongSuggest(3) {
            _songs.value = it
        }
    }


}