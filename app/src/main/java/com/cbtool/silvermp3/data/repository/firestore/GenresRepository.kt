package com.cbtool.silvermp3.data.repository.firestore

import com.cbtool.silvermp3.data.model.Genre
import com.cbtool.silvermp3.data.model.Song

class GenresRepository: BaseFirestoreRepository() {
    private val collectionName = "genres"
    private val collectionRef = db.collection(collectionName)
    fun add(genre: Genre){
        collectionRef.document(genre.name.lowercase()).set(genre)
    }
    fun getGenres(onResult: (List<Genre>) -> Unit){
        collectionRef
            .get()
            .addOnCompleteListener { snapshots ->
                if (snapshots.isSuccessful){
                    val genres = snapshots.result?.documents?.mapNotNull {
                        it.toObject(Genre::class.java)
                    } ?: emptyList()
                    onResult(genres)
                }else{
                    onResult(emptyList())
                }

            }
            .addOnFailureListener {
                onResult(emptyList())
            }

    }
}