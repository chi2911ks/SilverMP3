package com.cbtool.silvermp3.data.repository.firestore

import android.util.Log
import com.cbtool.silvermp3.data.model.Playlist
import com.cbtool.silvermp3.data.model.Song
import com.cbtool.silvermp3.interfaces.PlaylistRepository
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class PlaylistRepositoryImpl: PlaylistRepository {
    private val db = Firebase.firestore
    private val collectionName = "playlists"
    private val collection = db.collection(collectionName)
    override suspend fun getPlaylists(): List<Playlist> {
        return try {
            val snapshot = collection.get().await()
            snapshot.documents.mapNotNull {
                it.toObject(Playlist::class.java)
            }
        } catch (e: Exception) {
            Log.w(TAG, "Error getting documents.", e)
            emptyList()
        }
    }

    override suspend fun getSongs(playlistId: String): List<Song> {
        return try {
            val snapshot = collection.document(playlistId).collection("songs").get().await()
            snapshot.documents.mapNotNull {
                it.toObject(Song::class.java)
            }
        } catch (e: Exception) {
            Log.w(TAG, "Error getting documents.", e)
            emptyList()
        }
    }

    companion object {
        const val TAG = "PlaylistRepository"

    }
}