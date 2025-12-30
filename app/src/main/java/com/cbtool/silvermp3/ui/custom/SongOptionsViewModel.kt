package com.cbtool.silvermp3.ui.custom

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cbtool.silvermp3.data.model.Song
import com.cbtool.silvermp3.interfaces.UserFavouriteRepository

class SongOptionsViewModel(private val userFavouriteRepository: UserFavouriteRepository) :
    ViewModel() {
    private val _isFavourite = MutableLiveData<Boolean>()
    val isFavourite: MutableLiveData<Boolean> = _isFavourite
    fun toggleFavourite(song: Song) {
        userFavouriteRepository.toggleFavourite(song)
        _isFavourite.value = userFavouriteRepository.isFavourite(song.id)
    }

    fun checkFavourite(song: Song) {
        _isFavourite.value = userFavouriteRepository.isFavourite(song.id)
    }

}