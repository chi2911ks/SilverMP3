package com.cbtool.silvermp3.ui.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cbtool.silvermp3.data.model.Song
import com.cbtool.silvermp3.interfaces.UserFavouriteRepository

class PlayerViewModel(private val userFavouriteRepository: UserFavouriteRepository) : ViewModel() {
    private val _songs = MutableLiveData<List<Song>>()
    val songs: LiveData<List<Song>> = _songs

    private val _currentSong = MutableLiveData<Song>()
    val currentSong: LiveData<Song> = _currentSong
    private val _currentDuration = MutableLiveData<Int>()
    val currentDuration: LiveData<Int> = _currentDuration
    private val _isFavourite = MutableLiveData<Boolean>()
    val isFavourite: MutableLiveData<Boolean> = _isFavourite
    fun setSongs(song: List<Song>) {
        _songs.value = song
    }

    fun setCurrentSong(song: Song) {
        _currentSong.value = song
    }

    fun setCurrentDuration(duration: Int) {
        _currentDuration.value = duration
    }

    fun toggleFavourite(song: Song) {
        userFavouriteRepository.toggleFavourite(song)
        _isFavourite.value = userFavouriteRepository.isFavourite(song.id)
    }

    fun checkFavourite(id: String) {
        _isFavourite.value = userFavouriteRepository.isFavourite(id)
    }
}