package com.cbtool.silvermp3.data.model

import android.os.Parcelable
import androidx.appcompat.widget.DialogTitle
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class Playlist(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val coverUrl: String = "",
//    val userId: String = "",
    val createdAt: Timestamp = Timestamp.now(),
): Parcelable
