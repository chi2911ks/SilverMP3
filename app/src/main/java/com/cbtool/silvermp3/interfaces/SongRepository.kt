package com.cbtool.silvermp3.interfaces

import com.cbtool.silvermp3.data.model.Song

interface SongRepository {
    fun add(song: Song)
    fun getSongs(onResult: (List<Song>) -> Unit)
    suspend fun getSongSuggest(count: Int=5): List<Song>

}