package com.cbtool.silvermp3.interfaces

import com.cbtool.silvermp3.data.model.Artist

interface ArtistsRepository {
    fun add(artist: Artist)
    suspend fun getArtist(): List<Artist>
    suspend fun getPopularArtists(): List<Artist>


}