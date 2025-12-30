package com.cbtool.silvermp3.ui.auth

sealed class PhoneAuthState {
    object Idle : PhoneAuthState()
    object Loading : PhoneAuthState()
    data class CodeSent(val verificationId: String) : PhoneAuthState()
    data class Verified(val userId: String) : PhoneAuthState()
    data class Error(val message: String) : PhoneAuthState()
}
