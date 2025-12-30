package com.cbtool.silvermp3.interfaces

import com.cbtool.silvermp3.data.model.Song

interface SongRepository {
    fun add(song: Song)
    suspend fun getSongs(): List<Song>
    suspend fun getSongSuggest(count: Int = 5): List<Song>

}