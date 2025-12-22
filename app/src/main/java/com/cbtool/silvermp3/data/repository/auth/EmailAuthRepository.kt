package com.cbtool.silvermp3.data.repository.auth

import android.util.Log
import com.cbtool.silvermp3.data.state.LoginState

class EmailAuthRepository: BaseAuthRepository() {

    fun createAccount(email: String, password: String, onResult: (LoginState) -> Unit){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    onResult(LoginState.Success)
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    onResult(LoginState.Error(task.exception!!.message.toString()))
                }
            }
            .addOnFailureListener {
                onResult(LoginState.Error(it.message.toString()))
            }
    }
    fun loginAccount(email: String, password: String, onResult: (LoginState) -> Unit){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    onResult(LoginState.Success)
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    onResult(LoginState.Error(task.exception!!.message.toString()))
                }
            }.addOnFailureListener {
                onResult(LoginState.Error(it.message.toString()))
            }
    }


    companion object {
        const val TAG = "EmailAuthRepository"
    }
}