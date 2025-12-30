package com.cbtool.silvermp3.ui.auth.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cbtool.silvermp3.ui.auth.LoginState
import com.cbtool.silvermp3.data.repository.auth.EmailAuthRepository
import kotlinx.coroutines.launch


class EmailLoginViewModel(private val emailAuthRepository: EmailAuthRepository) : ViewModel() {
    private val _loginState = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState> = _loginState

    fun login(email: String, password: String) {
        _loginState.value = LoginState.Loading
        viewModelScope.launch {
            emailAuthRepository.loginAccount(email, password) {
                _loginState.value = it
            }
        }
    }


}