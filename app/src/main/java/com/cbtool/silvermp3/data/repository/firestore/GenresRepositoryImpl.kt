package com.cbtool.silvermp3.data.repository.firestore

import com.cbtool.silvermp3.data.model.Genre
import com.cbtool.silvermp3.interfaces.GenresRepository
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class GenresRepositoryImpl : GenresRepository {
    private val db = Firebase.firestore
    private val collectionName = "genres"
    private val collectionRef = db.collection(collectionName)
    override fun add(genre: Genre) {
        collectionRef.document(genre.name.lowercase()).set(genre)
    }

    override suspend fun getGenres(): List<Genre> {
        return try {
            collectionRef
                .get().await().toObjects(Genre::class.java)
        } catch (e: Exception) {
             emptyList()
        }
    }

}