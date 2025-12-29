package com.cbtool.silvermp3.data.repository.firestore

import android.util.Log
import com.cbtool.silvermp3.data.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class UsersRepositoryImpl {
    private val db = Firebase.firestore
    private val currentUser get() = Firebase.auth.currentUser!!
    private val collectionName = "users"
    private val collection = db.collection(collectionName)

    fun add() {
        collection.document(currentUser.uid).set(
            User(
                userId = currentUser.uid,
                name = currentUser.displayName ?: "",
                email = currentUser.email ?: "",
                numberPhone = currentUser.phoneNumber ?: "",
                avatarURL = currentUser.photoUrl.toString(),
                bio = ""
            )
        ).addOnSuccessListener {
            Log.d(TAG, "DocumentSnapshot added")
        }
        .addOnFailureListener {
            Log.w(TAG, "Error adding document", it)
        }
    }
    companion object {
        const val TAG = "UsersRepository"
    }
}