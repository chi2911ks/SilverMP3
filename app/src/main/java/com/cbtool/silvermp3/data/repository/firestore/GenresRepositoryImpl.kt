package com.cbtool.silvermp3.data.repository.firestore

import com.cbtool.silvermp3.data.model.Genre
import com.cbtool.silvermp3.interfaces.GenresRepository
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class GenresRepositoryImpl : GenresRepository {
    private val db = Firebase.firestore
    private val collectionName = "genres"
    private val collectionRef = db.collection(collectionName)
    override fun add(genre: Genre) {
        collectionRef.document(genre.name.lowercase()).set(genre)
    }

    override fun getGenres(onResult: (List<Genre>) -> Unit) {
        collectionRef
            .get()
            .addOnCompleteListener { snapshots ->
                if (snapshots.isSuccessful) {
                    val genres = snapshots.result?.documents?.mapNotNull {
                        it.toObject(Genre::class.java)
                    } ?: emptyList()
                    onResult(genres)
                } else {
                    onResult(emptyList())
                }

            }
            .addOnFailureListener {
                onResult(emptyList())
            }

    }
}