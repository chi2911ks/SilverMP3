package com.cbtool.silvermp3.data.repository.firestore

import android.util.Log
import com.cbtool.silvermp3.data.model.Song
import com.cbtool.silvermp3.interfaces.SongRepository
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class SongsRepositoryImpl: SongRepository {
    private val db = Firebase.firestore
    private val collectionName = "songs"
    private val collection = db.collection(collectionName)
    override fun add(song: Song){
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
    override fun getSongs(onResult: (List<Song>) -> Unit){
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
    override suspend fun getSongSuggest(count: Int): List<Song> {
        return try {
            val snapshot = collection.get().await()
            val songs = snapshot.documents.mapNotNull {
                it.toObject(Song::class.java)
            }
            val suggestedSongs = songs.shuffled().take(count)
            suggestedSongs
        }catch (e: Exception) {
            Log.w(TAG, "Error getting documents.", e)
            emptyList()
        }
    }

    companion object {
        const val TAG = "SongRepository"

    }
}