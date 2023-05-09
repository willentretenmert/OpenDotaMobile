package com.example.apiapplication.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    fun getAuth(login: String, password: String, callback: (Boolean) -> Unit) {
        auth.signInWithEmailAndPassword(login, password).addOnCompleteListener { task ->
                val loginResult = if (task.isSuccessful) task.isSuccessful
                else false
                callback(loginResult)
            }
    }
}