package com.cbtool.silvermp3.data.repository.firestore

import android.util.Log
import com.cbtool.silvermp3.data.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class UsersRepositoryImpl {
    private val db = Firebase.firestore
    private val currentUser get() = Firebase.auth.currentUser!!
    private val userId get() = currentUser.uid
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

    suspend fun getUser(): User? {
        return collection.document(userId).get().await().toObject(User::class.java)
    }

    fun delete() {
        // Delete the document
        collection.document(userId).delete()
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully deleted!")
            }
            .addOnFailureListener {
                Log.w(TAG, "Error deleting document", it)
            }
        // Delete the user
        currentUser.delete()

    }


    companion object {
        const val TAG = "UsersRepository"
    }
}