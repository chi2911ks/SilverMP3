package com.cbtool.silvermp3.data.model

data class Artist(
    var id: String = "",
    val name: String = "",
    val bio: String = "",
    val photoUrl: String = "",
    val genres: List<String> = emptyList(),
    val albumIds: List<String> = emptyList(),
)
