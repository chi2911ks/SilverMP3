package com.cbtool.silvermp3.data.model

data class User(
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val numberPhone: String = "",
    val avatarURL: String? = null,
    val bio: String = "",
)
