package com.cbtool.silvermp3.data.repository.auth

import com.google.firebase.Firebase
import com.google.firebase.auth.auth

abstract class BaseAuthRepository {
    protected val auth = Firebase.auth
    val currentUser get() = auth.currentUser!!

    fun isLoggedIn(): Boolean = auth.currentUser != null

    fun logout() = auth.signOut()

}