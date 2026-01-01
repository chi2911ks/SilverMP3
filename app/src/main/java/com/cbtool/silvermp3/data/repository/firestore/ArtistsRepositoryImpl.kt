package com.cbtool.silvermp3.data.repository.firestore

import android.util.Log
import com.cbtool.silvermp3.data.model.Artist
import com.cbtool.silvermp3.interfaces.ArtistsRepository
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await


class ArtistsRepositoryImpl : ArtistsRepository {
    private val db = Firebase.firestore
    private val collectionName = "artists"
    private val collectionRef = db.collection(collectionName)

    override fun add(artist: Artist) {
        val document = collectionRef.document()
        artist.id = document.id
        document.set(artist)
    }

     override suspend fun getArtist(): List<Artist> {
         return try {
             collectionRef.get().await().toObjects(Artist::class.java)
         } catch (e: Exception) {
             Log.w(TAG, "Error getting documents.", e)
             emptyList()

         }
    }

    override suspend fun getPopularArtists(): List<Artist> {
        return try {
            collectionRef.limit(6).get().await().toObjects(Artist::class.java)
        } catch (e: Exception) {
            Log.w(TAG, "Error getting documents.", e)
            emptyList()

        }
    }

    companion object {
        const val TAG = "ArtistsRepository"

    }
}