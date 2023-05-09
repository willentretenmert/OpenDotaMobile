package com.example.apiapplication.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apiapplication.data.Hero
import com.example.apiapplication.data.PlayersHeroStats
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.net.URL

class SearchViewModel: ViewModel() {
    private val gson = Gson()
    private val _heroes = MutableStateFlow<List<Hero>>(emptyList())

    fun fetchHeroes() {
        viewModelScope.launch(Dispatchers.IO) {
            val data = gson.fromJson(
                URL("https://api.opendota.com/api/heroes").readText(),
                Array<Hero>::class.java
            )
            _heroes.value = data.toList()
        }
    }

}