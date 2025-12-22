package com.cbtool.silvermp3.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cbtool.silvermp3.data.model.Genre
import com.cbtool.silvermp3.data.repository.firestore.GenresRepository

class SearchViewModel(private val genresRepository: GenresRepository): ViewModel() {
    private val _genres = MutableLiveData<List<Genre>>()
    val genres: LiveData<List<Genre>> = _genres
    fun getGenres(){
        genresRepository.getGenres{
            _genres.value = it
        }

    }
}