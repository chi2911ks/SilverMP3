package com.cbtool.silvermp3.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cbtool.silvermp3.data.model.User
import com.cbtool.silvermp3.data.repository.firestore.UsersRepositoryImpl
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepo: UsersRepositoryImpl,
) : ViewModel() {
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user
    fun getCurrentUser() {
        viewModelScope.launch {
            _user.value = userRepo.getUser()
        }

    }
    fun logout() {
        Firebase.auth.signOut()
    }
    fun deleteUser() {
        userRepo.delete()
    }
}