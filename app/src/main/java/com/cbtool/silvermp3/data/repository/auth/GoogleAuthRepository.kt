package com.cbtool.silvermp3.data.repository.auth


import android.content.Context

import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.ClearCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.cbtool.silvermp3.R
import com.cbtool.silvermp3.data.state.LoginState

import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.auth.GoogleAuthProvider

class GoogleAuthRepository(
    private val context: Context,
): BaseAuthRepository() {
    private lateinit var credentialManager: CredentialManager
    private var request: GetCredentialRequest
    private lateinit var credential: Credential
    init {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(context.getString(R.string.default_web_client_id))
            .setFilterByAuthorizedAccounts(false)
            .build()
        request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
    }
    suspend fun createCredential() {
        credentialManager = CredentialManager.create(context)
        val result = credentialManager.getCredential(context = context, request = request)
        credential = result.credential
    }
    suspend fun signInGoogle(onResult: (LoginState) -> Unit){
        try {
            createCredential()
            if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                firebaseAuthWithGoogle(googleIdTokenCredential.idToken){
                    onResult(it)
                }
            } else {
                Log.w(TAG, "Credential is not of type Google ID!")
                onResult(LoginState.Error("Credential is not of type Google ID!"))
            }
        }
        catch (_: NoCredentialException){
            onResult(LoginState.Error("Vui lòng đăng nhập google trước!"))
        }
        catch (e: Exception){
            Log.w(TAG, "signInGoogle:failure", e)
            onResult(LoginState.Error(e.message.toString()))

        }


    }

    private fun firebaseAuthWithGoogle(idToken: String, onResult: (LoginState) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    onResult(LoginState.Success)

                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    onResult(LoginState.Error(task.exception!!.message.toString()))
                }
            }.addOnFailureListener {
                onResult(LoginState.Error(it.message.toString()))
            }
    }
    suspend fun signOut() {
        // Firebase sign out
        auth.signOut()

        // When a user signs out, clear the current user credential state from all credential providers.

        try {
            val clearRequest = ClearCredentialStateRequest()
            credentialManager.clearCredentialState(clearRequest)
        } catch (e: ClearCredentialException) {
            Log.e(TAG, "Couldn't clear user credentials: ${e.localizedMessage}")
        }

    }

    companion object {
        const val TAG = "GoogleAuthRepository"
    }
}