package com.cbtool.silvermp3.ui.library

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cbtool.silvermp3.data.model.Song
import com.cbtool.silvermp3.data.repository.firestore.UserFavouriteRepository
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class FavouriteViewModel(private val userFavouriteRepository: UserFavouriteRepository) : ViewModel() {
    private val _songs = MutableLiveData<List<Song>>()
    val song: LiveData<List<Song>> = _songs
    fun getSongs(){

        Log.d("FavouriteVM", "scope active = ${viewModelScope.isActive}")
        viewModelScope.launch {
            val songs = userFavouriteRepository.getSongs()
            _songs.value = songs
        }
    }
}