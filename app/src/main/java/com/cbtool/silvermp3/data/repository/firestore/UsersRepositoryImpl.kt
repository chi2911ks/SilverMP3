package com.cbtool.silvermp3.data.repository.firestore

import android.util.Log
import com.cbtool.silvermp3.data.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class UsersRepositoryImpl {
    private val db = Firebase.firestore
    private val auth = Firebase.auth
    private val currentUser get() = auth.currentUser!!
    private val userId get() = currentUser.uid
    private val collectionName = "users"
    private val collection = db.collection(collectionName)

    fun add() {
        collection.document(currentUser.uid).set(
            User(
                userId = currentUser.uid,
                name = currentUser.displayName ?: "",
                email = currentUser.email ?: "",
                numberPhone = currentUser.phoneNumber ?: "",
                avatarURL = currentUser.photoUrl.toString(),
                bio = ""
            )
        ).addOnSuccessListener {
            Log.d(TAG, "DocumentSnapshot added")
        }
            .addOnFailureListener {
                Log.w(TAG, "Error adding document", it)
            }
    }

    suspend fun getUser(): User? {
        return collection.document(userId).get().await().toObject(User::class.java)
    }
    // Sửa hàm delete thành suspend để đảm bảo xóa xong dữ liệu mới xóa User
    suspend fun delete() {

        try {

            val userDocRef = collection.document(userId)

            // 1. DANH SÁCH CÁC COLLECTION CON CẦN XÓA
            // Bạn CẦN điền tên chính xác các collection con trong user của bạn vào đây
            // Ví dụ: "playlists", "favorites", "songs"...
            val subCollections = listOf("playlists", "favourites", "songsInPlaylists")

            // 2. Lặp qua từng collection để xóa document bên trong
            subCollections.forEach { subColName ->
                deleteCollection(userDocRef.collection(subColName))
            }

            // 3. Xóa document User cha (DocumentSnapshot)
            userDocRef.delete().await()
            Log.d(TAG, "User document successfully deleted!")

            // 4. Cuối cùng mới xóa tài khoản Authentication
            currentUser.delete().await()
            Log.d(TAG, "Auth account successfully deleted!")

        } catch (e: Exception) {
            Log.e(TAG, "Error deleting account", e)
            throw e // Ném lỗi ra để ViewModel xử lý (hiện thông báo)
        }

    }

    // Hàm hỗ trợ xóa sạch một collection (xử lý Batch > 500 item)
    private suspend fun deleteCollection(collectionRef: com.google.firebase.firestore.CollectionReference) {
        try {
            // Lấy 500 document mỗi lần (giới hạn của Batch Write)
            val batchSize = 500L

            while (true) {
                // Lấy danh sách documents
                val snapshot = collectionRef.limit(batchSize).get().await()

                if (snapshot.isEmpty) {
                    break // Hết dữ liệu để xóa
                }

                val batch = db.batch()
                for (document in snapshot.documents) {
                    batch.delete(document.reference)
                }

                // Thực thi xóa
                batch.commit().await()
                Log.d(TAG, "Deleted batch of ${snapshot.size()} items in ${collectionRef.path}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting collection: ${collectionRef.path}", e)
        }
    }



    companion object {
        const val TAG = "UsersRepository"
    }
}