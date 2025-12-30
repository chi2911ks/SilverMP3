package com.cbtool.silvermp3.data.repository.firestore

import android.util.Log
import com.cbtool.silvermp3.data.model.Artist
import com.cbtool.silvermp3.interfaces.ArtistsRepository
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore


class ArtistsRepositoryImpl : ArtistsRepository {
    private val db = Firebase.firestore
    private val collectionName = "artists"
    private val collectionRef = db.collection(collectionName)

    override fun add(artist: Artist) {
        val document = collectionRef.document()
        artist.id = document.id
        document.set(artist)
    }

    override fun getAll(onResult: (List<Artist>) -> Unit) {
        collectionRef.get()
            .addOnCompleteListener { snapshot ->
                if (snapshot.isSuccessful) {
                    val artists = snapshot.result.documents.mapNotNull { document ->
                        val artist = document.toObject(Artist::class.java)
                        artist?.apply { id = document.id }
                    }
                    onResult(artists)
                } else {
                    onResult(emptyList())
                    Log.w(TAG, "Error getting documents.", snapshot.exception)
                }
            }
            .addOnFailureListener {
                onResult(emptyList())
                Log.w(TAG, "Error getting documents.", it)
            }
    }

    companion object {
        const val TAG = "ArtistsRepository"

    }
}