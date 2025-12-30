package com.cbtool.silvermp3.data.repository.firestore

import android.util.Log
import com.cbtool.silvermp3.data.model.Playlist
import com.cbtool.silvermp3.data.model.Song
import com.cbtool.silvermp3.interfaces.UserPlaylistRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class UserPlaylistRepositoryImpl : UserPlaylistRepository {
    private val db = Firebase.firestore
    private val currentUser get() = Firebase.auth.currentUser!!
    private val userId get() = currentUser.uid
    private val userRef = db.collection("users").document(userId)
    private val playlistsRef = db.collection("users").document(userId).collection("playlists")

    override fun addSong(playlistId: String, song: Song) {

        val playlistSongRef = playlistsRef.document(playlistId)
            .collection("songs").document(song.id)
        val reverseIndexRef = userRef.collection("songsInPlaylists").document(song.id)

        db.runBatch { batch ->
            // 1️⃣ Thêm bài vào playlist
            batch.set(playlistSongRef, song) // ghi dữ liệu song
            batch.set(
                playlistSongRef,
                mapOf("addedAt" to FieldValue.serverTimestamp()),
                SetOptions.merge()
            )

            // 2️⃣ Cập nhật chỉ mục ngược
            batch.set(
                reverseIndexRef, mapOf(
                    "playlists" to FieldValue.arrayUnion(playlistId)
                ), SetOptions.merge()
            )
        }
    }

    override fun removeSong(playlistId: String, songId: String) {
        val playlistSongRef = playlistsRef.document(playlistId)
            .collection("songs").document(songId)
        val reverseIndexRef = userRef
            .collection("songsInPlaylists").document(songId)

        db.runBatch { batch ->
            // 1️⃣ Xóa bài hát khỏi playlist
            batch.delete(playlistSongRef)

            // 2️⃣ Gỡ playlist này khỏi danh sách playlists trong chỉ mục ngược
            batch.update(reverseIndexRef, "playlists", FieldValue.arrayRemove(playlistId))
        }.addOnSuccessListener {
            Log.d("Firestore", "✅ Đã xóa bài $songId khỏi playlist $playlistId và cập nhật index")
        }.addOnFailureListener { e ->
            Log.e("Firestore", "❌ Lỗi khi xóa bài: ${e.message}", e)
        }
    }


    override fun create(name: String) {
        val doc = playlistsRef.document()
        doc.set(
            Playlist(
                id = doc.id,
                title = name
            )
        )
    }

    override fun remove(playlistId: String) {
        playlistsRef.document(playlistId).delete()
    }

    override fun update(playlist: Playlist) {
        playlistsRef.document(playlist.id).set(playlist)
    }

    override fun update(playlistId: String, name: String, desc: String) {
        playlistsRef.document(playlistId).update(
            mapOf(
                "title" to name,
                "description" to desc
            )
        )
    }

    override suspend fun getPlaylists(): List<Playlist> {

        return try {
            playlistsRef.orderBy("createdAt").get().await().toObjects(Playlist::class.java)

        } catch (e: Exception) {
            Log.w(TAG, "Error getting documents.", e)
            emptyList()
        }

    }

    override suspend fun getPlaylist(id: String): Playlist {
        return try {
            playlistsRef.document(id).get().await()
                .toObject(Playlist::class.java) ?: Playlist()
        } catch (e: Exception) {
            Log.w(TAG, "Error getting documents.", e)
            Playlist()
        }

    }

    override suspend fun getSongs(playlistId: String): List<Song> {
        return try {
            playlistsRef
                .document(playlistId)
                .collection("songs")
                .orderBy("addedAt")
                .get()
                .await().toObjects(Song::class.java)
        } catch (e: Exception) {
            Log.w(TAG, "Error getting documents.", e)
            emptyList()
        }
    }

    override suspend fun getPlaylistsContainingSong(songId: String): List<String> {
        val reverseIndexRef = userRef.collection("songsInPlaylists").document(songId)
        return try {
            val playlists = reverseIndexRef.get().await().get("playlists") as? List<*>
            return playlists?.filterIsInstance<String>() ?: emptyList()

        } catch (e: Exception) {
            Log.e("Firestore", "❌ Lỗi khi kiểm tra playlists chứa songId=$songId: ${e.message}")
            emptyList()
        }
    }


    companion object {
        const val TAG = "UserPlaylistRepository"
    }
}