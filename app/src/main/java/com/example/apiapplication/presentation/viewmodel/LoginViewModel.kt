package com.example.apiapplication.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.apiapplication.presentation.ui.activity.MainActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoginViewModel : ViewModel() {

    private val _isLoginSuccessful = MutableStateFlow<Boolean?>(null)
    val isLoginSuccessful: StateFlow<Boolean?> = _isLoginSuccessful

    private val auth = MainActivity.User.auth
    fun getAuth(login: String, password: String, callback: (Boolean) -> Unit) {
        auth.signInWithEmailAndPassword(login, password).addOnCompleteListener { task ->
            callback(task.isSuccessful)
        }
    }

    fun checkAuth() {
        _isLoginSuccessful.value = auth.currentUser != null
    }
}