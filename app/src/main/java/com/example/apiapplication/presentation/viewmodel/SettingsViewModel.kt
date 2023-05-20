package com.example.apiapplication.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.apiapplication.data.api.RaspberryAPI
import com.example.apiapplication.data.models.FirebaseUserRaspberry
import com.example.apiapplication.data.models.PlayersProfile
import com.example.apiapplication.networking.RaspberryPiProvider
import com.example.apiapplication.presentation.ui.activity.MainActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel : ViewModel(){

    private val auth = MainActivity.User.auth

    private val raspberryApi = RaspberryPiProvider()

    val firebaseProfile: StateFlow<FirebaseUserRaspberry?> = raspberryApi.firebaseProfile

    fun logOut(){
        auth.signOut()
    }

    fun fetchFirebaseProfile(email : String) {
        raspberryApi.fetchFirebaseProfile(email)
    }

    fun updateNickname(userMail: String, nickname: String, callback : (Boolean) -> Unit) {
        raspberryApi.updateNickname(userMail, nickname, callback)
    }
}