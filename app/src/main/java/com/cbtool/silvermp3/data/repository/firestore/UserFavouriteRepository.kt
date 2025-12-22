package com.cbtool.silvermp3.data.repository.firestore

import android.util.Log
import com.cbtool.silvermp3.data.model.Song
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.tasks.await

class UserFavouriteRepository: BaseFirestoreRepository() {
    private val userId get() = currentUser!!.uid
    private val collectionRef = db.collection("users").document(userId).collection("favourites")
    private var favouriteListener: ListenerRegistration? = null
    private val favouriteSongsCache = mutableSetOf<String>()
    init {
        observeFavouriteSongs()
    }
    fun addSong(song: Song) {
        collectionRef.document(song.id).set(song)
    }
    fun removeSong(songId: String){
        collectionRef.document(songId).delete()
    }
    suspend fun getCount(): Int {
        return try {
            val snapshot = collectionRef.get().await()  // chờ Firestore query hoàn thành
            snapshot.size() // trả về số lượng document
        } catch (e: Exception) {
            Log.w(TAG, "Error getting documents.", e)
            0
        }
    }
    suspend fun getSongs(): List<Song> {
        Log.d(TAG, "getSongs() started")
        return try {
            val snapshot = collectionRef.get()
                .await()
                .documents.mapNotNull { it.toObject(Song::class.java)  }
            Log.w(TAG, "OK")
            snapshot
        }catch (e: Exception){
            Log.w(TAG, "Error getting documents.", e)
            emptyList()
        }
    }
    fun getSongsRealtime(onResult: (List<Song>) -> Unit) {
        collectionRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            val songs = snapshot?.documents?.mapNotNull {
                it.toObject(Song::class.java)
            } ?: emptyList()

            onResult(songs)
        }
    }

    fun observeFavouriteSongs() {
        favouriteListener?.remove()
        favouriteListener = collectionRef.addSnapshotListener { snapshot, error ->
            if (snapshot != null) {
                favouriteSongsCache.clear()
                favouriteSongsCache.addAll(snapshot.documents.mapNotNull { it.id })
            }
        }
    }
    fun toggleFavourite(song: Song) {
        song.apply {
            if (isFavourite(id)) {
                removeSong(id)
                favouriteSongsCache.remove(id)
            } else {
                addSong(this)
                favouriteSongsCache.add(id)
            }
        }

    }
    fun stopObservingFavourites() {
        favouriteListener?.remove()
        favouriteListener = null
    }

    fun isFavourite(songId: String): Boolean {
        return favouriteSongsCache.contains(songId)
    }
    fun clearCache() {
        favouriteSongsCache.clear()
    }

    companion object {
        const val TAG = "UserFavouriteRepository"

    }
}