package com.cbtool.silvermp3.interfaces

import com.cbtool.silvermp3.data.model.Playlist

interface PlaylistRepository {
    suspend fun getPlaylists(): List<Playlist>
}