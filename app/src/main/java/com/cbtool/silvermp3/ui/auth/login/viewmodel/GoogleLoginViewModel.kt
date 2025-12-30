package com.cbtool.silvermp3.ui.auth.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cbtool.silvermp3.data.state.LoginState
import com.cbtool.silvermp3.data.repository.auth.GoogleAuthRepository
import com.cbtool.silvermp3.data.repository.firestore.UsersRepositoryImpl
import kotlinx.coroutines.launch

class GoogleLoginViewModel(
    private val googleAuthRepository: GoogleAuthRepository,
    private val usersRepository: UsersRepositoryImpl
) : ViewModel() {
    private val _loginState = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState> = _loginState
    fun login() {
        _loginState.value = LoginState.Loading
        viewModelScope.launch {
            googleAuthRepository.signInGoogle {
                if (it is LoginState.Success) {
                    usersRepository.add()
                }
                _loginState.value = it
            }

        }

    }
}