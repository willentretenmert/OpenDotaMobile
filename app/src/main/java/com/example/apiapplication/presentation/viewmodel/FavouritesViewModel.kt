package com.example.apiapplication.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.apiapplication.data.api.RaspberryAPI
import com.example.apiapplication.data.models.Hero
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FavouritesViewModel : ViewModel() {

    private val _heroes = MutableStateFlow<List<Hero>>(emptyList())
    val heroes: StateFlow<List<Hero>> = _heroes

    // http://176.99.158.188:50993/request?key=dotaProfile&id=275690206
    // http://176.99.158.188:50993/request?key=firebaseProfile&id=12345


    private val retrofit = Retrofit.Builder()
        .baseUrl("http://176.99.158.188:50993/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(RaspberryAPI::class.java)

}