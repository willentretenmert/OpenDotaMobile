package com.example.apiapplication.presentation.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.apiapplication.presentation.ui.activity.MainActivity
import com.google.firebase.auth.FirebaseAuth

class SignUpViewModel : ViewModel() {

    private val auth = MainActivity.User.auth

    fun signUp(login: String, password: String, cont: Context?) {
        auth.createUserWithEmailAndPassword(login.toString(), password.toString()).addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                Toast.makeText(cont, "Registration Successful.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(cont, "Registration failed.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}