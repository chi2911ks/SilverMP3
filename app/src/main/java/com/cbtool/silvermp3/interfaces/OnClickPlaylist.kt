package com.cbtool.silvermp3.interfaces

import com.cbtool.silvermp3.data.model.Playlist

interface OnClickPlaylist {
    fun onClickPlaylist(playlist: Playlist)
    fun onClickFavourite()
}