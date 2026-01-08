package com.cbtool.silvermp3.ui.player

import com.cbtool.silvermp3.data.model.Song

object PlaybackState {
    var currentPlaylist: List<Song> = mutableListOf()
    var currentIndex = 0
    var currentSourcePlaying: Map<String, Boolean> = hashMapOf()
}
