package com.cbtool.silvermp3.ui.auth.register.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cbtool.silvermp3.ui.auth.LoginState
import com.cbtool.silvermp3.data.repository.auth.EmailAuthRepository
import com.cbtool.silvermp3.data.repository.firestore.UsersRepositoryImpl
import kotlinx.coroutines.launch

class EmailRegisterViewModel(
    private val emailAuthRepository: EmailAuthRepository,
    private val usersRepository: UsersRepositoryImpl
) : ViewModel() {
    private val _loginState = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState> = _loginState

    fun register(email: String, password: String) {
        _loginState.value = LoginState.Loading
        viewModelScope.launch {
            emailAuthRepository.createAccount(email, password) {
                if (it is LoginState.Success) {
                    usersRepository.add()
                }
                _loginState.value = it
            }
        }
    }
}