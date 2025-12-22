package com.cbtool.silvermp3.data.model

sealed class LibraryItem{
    data class PlaylistItem(val playlist: Playlist): LibraryItem()
    data class FavouriteItem(val count: Int): LibraryItem()
}