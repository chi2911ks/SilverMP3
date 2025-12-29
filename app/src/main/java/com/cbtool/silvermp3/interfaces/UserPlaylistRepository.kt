package com.cbtool.silvermp3.interfaces

import com.cbtool.silvermp3.data.model.Playlist
import com.cbtool.silvermp3.data.model.Song

interface UserPlaylistRepository {
    fun addSong(playlistId: String, song: Song)
    fun removeSong(playlistId: String, songId: String)
    fun create(name: String)
    fun remove(playlistId: String)
    fun update(playlist: Playlist)
    fun update(playlistId: String, name: String, desc: String)
    suspend fun getPlaylists(): List<Playlist>
    suspend fun getPlaylist(id: String): Playlist
    suspend fun getSongs(playlistId: String): List<Song>
    suspend fun getPlaylistsContainingSong(songId: String): List<String>
}