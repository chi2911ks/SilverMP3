package com.cbtool.silvermp3.data.repository.auth

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cbtool.silvermp3.ui.auth.PhoneAuthState
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class PhoneAuthRepository : BaseAuthRepository() {
    private var storedVerificationId: String? = null
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null
    private val _authState = MutableLiveData<PhoneAuthState>()
    val authState: LiveData<PhoneAuthState> = _authState
    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            Log.d(TAG, "onVerificationCompleted:$credential")
            _authState.value = PhoneAuthState.Verified(credential.toString())
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Log.w(TAG, "onVerificationFailed", e)

            when (e) {
                is FirebaseAuthInvalidCredentialsException -> {
                    // Invalid request
                    _authState.value = PhoneAuthState.Error(e.message.toString())
                }

                is FirebaseTooManyRequestsException -> {
                    // The SMS quota for the project has been exceeded
                    _authState.value = PhoneAuthState.Error(e.message.toString())
                }

                is FirebaseAuthMissingActivityForRecaptchaException -> {
                    // reCAPTCHA verification attempted with null Activity
                    _authState.value = PhoneAuthState.Error(e.message.toString())
                }
            }

            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
            Log.d(TAG, "onCodeSent:$verificationId")
            _authState.value = PhoneAuthState.CodeSent(verificationId)
            storedVerificationId = verificationId
            resendToken = token
            Log.d(TAG, "resendToken:$token")
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {

        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    _authState.value = PhoneAuthState.Verified(credential.toString())
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    _authState.value = PhoneAuthState.Error(task.exception.toString())
//                    when (task.exception) {
//
//                        is FirebaseAuthInvalidCredentialsException -> {
//                            // The verification code entered was invalid
//                            _authState.value = PhoneAuthState.Error(task.exception.toString())
//                        }
//
//                        else -> {
//                            _authState.value = PhoneAuthState.Error(task.exception.toString())
//                        }
//                    }
                }
            }
    }

    fun verifyCode(code: String) {
        _authState.value = PhoneAuthState.Loading
        val credential = PhoneAuthProvider.getCredential(storedVerificationId!!, code)
        signInWithPhoneAuthCredential(credential)
    }

    fun verifyPhoneNumber(phoneNumber: String, activity: Activity) {
        _authState.value = PhoneAuthState.Loading
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
        resendToken?.let {
            options.setForceResendingToken(it)
        }
        PhoneAuthProvider.verifyPhoneNumber(options.build())
    }

    companion object {
        const val TAG = "PhoneAuthRepository"
    }
}