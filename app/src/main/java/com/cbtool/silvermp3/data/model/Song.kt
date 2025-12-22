package com.cbtool.silvermp3.data.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class Song(
    var id: String = "",
    val title: String = "",
    val artistId: String = "",
    val artistName: String = "",
    val albumId: String? = null,
    val coverUrl: String = "",
    val audioUrl: String = "",
    val duration: Long = 0,
    val genres: List<String> = emptyList(),
    val listens: Long = 0,
    val releaseDate: Timestamp = Timestamp.now(),
) : Parcelable
