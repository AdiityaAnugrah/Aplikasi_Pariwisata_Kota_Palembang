package com.adityaanugrah.skripsiku.retrofit

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FirebaseService {
    companion object {
        private var auth = Firebase.auth

        fun login(email: String, password: String, onResult: (ResultState) -> Unit) {
            onResult(ResultState.Loading)
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    onResult(ResultState.Success)
                } else {
                    onResult(ResultState.Error)
                }
            }
        }

        fun register(email: String,
                     password: String,
                     onResult: (ResultState) -> Unit) {
            onResult(ResultState.Loading)
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    onResult(ResultState.Success)
                }
                else {
                    onResult(ResultState.Error)
                }
            }
        }
    }
}

sealed class ResultState {
    object Success : ResultState()
    object Error : ResultState()
    object Loading : ResultState()
}