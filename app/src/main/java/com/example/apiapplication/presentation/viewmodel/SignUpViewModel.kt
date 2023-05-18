package com.example.apiapplication.presentation.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.apiapplication.presentation.ui.activity.MainActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SignUpViewModel : ViewModel() {

    private val auth = MainActivity.User.auth
    private val _isSignUpSuccessful = MutableStateFlow<Boolean?>(null)
    val isSignUpSuccessful: StateFlow<Boolean?> = _isSignUpSuccessful

    fun signUp(login: String, password: String, cont: Context?) {
        auth.createUserWithEmailAndPassword(login, password).addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                auth.signOut()
                Toast.makeText(cont, "Registration Successful.", Toast.LENGTH_SHORT).show()
                _isSignUpSuccessful.value = true
            }
            else {
                Toast.makeText(cont, "Registration failed.", Toast.LENGTH_SHORT).show()
                _isSignUpSuccessful.value = false
            }
        }
    }
}