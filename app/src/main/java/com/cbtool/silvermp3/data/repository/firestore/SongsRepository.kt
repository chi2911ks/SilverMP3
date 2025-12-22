package com.cbtool.silvermp3.data.repository.firestore

import android.util.Log
import com.cbtool.silvermp3.data.model.Song

class SongsRepository: BaseFirestoreRepository() {
    private val collectionName = "songs"
    private val collection = db.collection(collectionName)
    fun add(song: Song){
        val doc = collection.document()
        song.id = doc.id
        doc.set(song)
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot added with ID: ${doc.id}")
            }
            .addOnFailureListener {
                Log.w(TAG, "Error adding document", it)
            }
    }
    fun getSongs(onResult: (List<Song>) -> Unit){
        collection
            .get()
            .addOnCompleteListener { snapshot->
                if (snapshot.isSuccessful){
                    val songs = snapshot.result.documents.mapNotNull {
                        it.toObject(Song::class.java)
                    }
                    onResult(songs)

                }else{
                    Log.w(TAG, "Error getting documents.", snapshot.exception)
                    onResult(emptyList())
                }
            }
            .addOnFailureListener {
                Log.w(TAG, "Error getting documents.", it)
                onResult(emptyList())
            }
    }
    fun getSongSuggest(count: Int=5, onResult: (List<Song>) -> Unit) {
        collection.get().addOnCompleteListener { snapshot ->
            if (snapshot.isSuccessful) {
                val songs = snapshot.result?.documents?.mapNotNull {
                    it.toObject(Song::class.java)
                } ?: emptyList()

                val suggestedSongs = songs.shuffled().take(count)

                onResult(suggestedSongs)
            }
        }
    }
    fun getSongByGenre(genre: String, onResult: (List<Song>) -> Unit) {

    }

    companion object {
        const val TAG = "SongRepository"

    }
}