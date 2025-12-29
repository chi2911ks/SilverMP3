package com.cbtool.silvermp3.interfaces

import com.cbtool.silvermp3.data.model.Artist

interface ArtistsRepository {
    fun add(artist: Artist)
    fun getAll(onResult: (List<Artist>) -> Unit)
}