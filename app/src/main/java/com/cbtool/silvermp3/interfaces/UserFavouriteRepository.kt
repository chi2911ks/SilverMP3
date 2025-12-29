package com.cbtool.silvermp3.interfaces

import com.cbtool.silvermp3.data.model.Song

interface UserFavouriteRepository {
    fun addSong(song: Song)
    fun removeSong(songId: String)
    suspend fun getCount(): Int
    suspend fun getSongs(): List<Song>
    fun isFavourite(songId: String): Boolean
    fun toggleFavourite(song: Song)
}