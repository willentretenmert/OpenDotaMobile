package com.example.apiapplication.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.apiapplication.presentation.ui.activity.MainActivity
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel() {

    private val auth = MainActivity.User.auth
    fun getAuth(login: String, password: String, callback: (Boolean) -> Unit) {
        auth.signInWithEmailAndPassword(login, password).addOnCompleteListener { task ->
            callback(task.isSuccessful)
        }
    }

    fun checkAuth() : Boolean{
        return auth.currentUser != null
    }
}