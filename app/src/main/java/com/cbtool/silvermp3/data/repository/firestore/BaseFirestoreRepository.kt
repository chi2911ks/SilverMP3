package com.cbtool.silvermp3.data.repository.firestore

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

abstract class BaseFirestoreRepository {
    val db = Firebase.firestore

    val currentUser get() = Firebase.auth.currentUser
}