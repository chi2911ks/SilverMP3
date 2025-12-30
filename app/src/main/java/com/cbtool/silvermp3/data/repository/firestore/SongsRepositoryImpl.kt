package com.cbtool.silvermp3.data.repository.firestore

import android.util.Log
import com.cbtool.silvermp3.data.model.Song
import com.cbtool.silvermp3.interfaces.SongRepository
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class SongsRepositoryImpl : SongRepository {
    private val db = Firebase.firestore
    private val collectionName = "songs"
    private val collection = db.collection(collectionName)
    override fun add(song: Song) {
        val doc = collection.document()
        song.id = doc.id
        doc.set(song)
    }

    override suspend fun getSongs(): List<Song> {
        return try {
            val snapshot = collection.get().await()
            snapshot.toObjects(Song::class.java)
        } catch (e: Exception) {
            Log.w(TAG, "Error getting documents.", e)
            emptyList()
        }

    }

    override suspend fun getSongSuggest(count: Int): List<Song> {
        return try {
            // Chỉ lấy tối đa 50 bài mới nhất thay vì lấy toàn bộ
            val snapshot = collection
                .orderBy("releaseDate", Query.Direction.DESCENDING)
                .limit(50)
                .get()
                .await()

            val songs = snapshot.toObjects(Song::class.java)
            songs.shuffled().take(count)
        } catch (e: Exception) {
            emptyList()
        }
    }

    companion object {
        const val TAG = "SongRepository"

    }
}