package com.example.apiapplication.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.apiapplication.presentation.ui.activity.MainActivity

class SettingsViewModel : ViewModel(){

    private val auth = MainActivity.User.auth

    fun logOut(){
        auth.signOut()
    }
}