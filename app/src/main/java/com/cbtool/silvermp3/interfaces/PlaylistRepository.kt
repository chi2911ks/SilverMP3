package com.cbtool.silvermp3.interfaces

import com.cbtool.silvermp3.data.model.Playlist
import com.cbtool.silvermp3.data.model.Song

interface PlaylistRepository {
    suspend fun getPlaylists(): List<Playlist>
    suspend fun getDetailPlaylist(playlistId: String): Playlist
    suspend fun getSongs(playlistId: String): List<Song>
}