package com.cbtool.silvermp3.data.repository.firestore

import android.util.Log
import com.cbtool.silvermp3.data.model.Song
import com.cbtool.silvermp3.interfaces.UserFavouriteRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class UserFavouriteRepositoryImpl: UserFavouriteRepository {
    private val db = Firebase.firestore
    private val currentUser get() = Firebase.auth.currentUser!!
    private val userId get() = currentUser.uid
    private val collectionRef = db.collection("users").document(userId).collection("favourites")
    private var favouriteListener: ListenerRegistration? = null
    private val favouriteSongsCache = mutableSetOf<String>()
    init {
        observeFavouriteSongs()
    }
    override fun addSong(song: Song) {
        collectionRef.document(song.id).set(song)
    }
    override fun removeSong(songId: String){
        collectionRef.document(songId).delete()
    }
    override suspend fun getCount(): Int {
        return try {
            val snapshot = collectionRef.get().await()  // chờ Firestore query hoàn thành
            snapshot.size() // trả về số lượng document
        } catch (e: Exception) {
            Log.w(TAG, "Error getting documents.", e)
            0
        }
    }
    override suspend fun getSongs(): List<Song> {
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
    override fun toggleFavourite(song: Song) {
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

    override fun isFavourite(songId: String): Boolean {
        return favouriteSongsCache.contains(songId)
    }
    fun clearCache() {
        favouriteSongsCache.clear()
    }

    companion object {
        const val TAG = "UserFavouriteRepository"

    }
}