package com.cbtool.silvermp3.interfaces

import com.cbtool.silvermp3.data.model.Playlist

interface OnClickAddPlaylist {
    fun playlist(playlist: Playlist, isAdd: Boolean)
    fun favourite(isAdd: Boolean)
}