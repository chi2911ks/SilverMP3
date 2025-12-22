package com.cbtool.silvermp3.ui.auth.login.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.cbtool.silvermp3.data.repository.auth.PhoneAuthRepository
import com.cbtool.silvermp3.data.repository.firestore.UsersRepository

class PhoneAuthViewModel(private val phoneAuthRepository: PhoneAuthRepository, private val usersRepository: UsersRepository): ViewModel() {

    val authState = phoneAuthRepository.authState

    fun verifyPhoneNumber(phoneNumber: String, activity: Activity){
        phoneAuthRepository.verifyPhoneNumber(phoneNumber, activity)

    }
    fun verifyCode(code: String) {
        phoneAuthRepository.verifyCode(code)
    }
    fun addUser(){
        usersRepository.add()
    }
}