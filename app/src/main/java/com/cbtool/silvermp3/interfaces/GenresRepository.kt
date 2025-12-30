package com.cbtool.silvermp3.interfaces

import com.cbtool.silvermp3.data.model.Genre

interface GenresRepository {
    fun add(genre: Genre)
    suspend fun getGenres(): List<Genre>
}