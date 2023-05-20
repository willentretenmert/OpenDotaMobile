package com.example.apiapplication.presentation.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.apiapplication.networking.RaspberryPiProvider
import com.example.apiapplication.presentation.ui.activity.MainActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.log

class SignUpViewModel : ViewModel() {

    private val raspberryProvider = RaspberryPiProvider()

    private val auth = MainActivity.User.auth
    private val _isSignUpSuccessful = MutableStateFlow<Boolean?>(null)
    val isSignUpSuccessful: StateFlow<Boolean?> = _isSignUpSuccessful

    fun signUp(login: String, password: String, cont: Context?) {
        auth.createUserWithEmailAndPassword(login, password).addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                auth.signOut()
                Toast.makeText(cont, "Registration Successful.", Toast.LENGTH_SHORT).show()
                _isSignUpSuccessful.value = true
                createNewProfile(login)
            }
            else {
                Toast.makeText(cont, "Registration failed.", Toast.LENGTH_SHORT).show()
                _isSignUpSuccessful.value = false
            }
        }
    }

    private fun createNewProfile(login : String) {
        raspberryProvider.postNewProfile(login) {
                success -> Log.d("zxc", success.toString())
        }
    }
}