package com.example.apiapplication.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apiapplication.data.Hero
import com.example.apiapplication.data.PlayersHeroStats
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL

class MainViewModel : ViewModel() {

    private val gson = Gson()
    private val _heroes = MutableLiveData<List<Hero>>()
    val heroes: LiveData<List<Hero>> = _heroes

    private val _playersHeroStats = MutableLiveData<List<PlayersHeroStats>>()
    val playersHeroStats: LiveData<List<PlayersHeroStats>> = _playersHeroStats

    fun fetchHeroes() {
        viewModelScope.launch(Dispatchers.IO) {
            val data = gson.fromJson(
                URL("https://api.opendota.com/api/heroes").readText(),
                Array<Hero>::class.java
            )
            _heroes.postValue(data.toList())
        }
    }

    fun fetchPlayerStats(id: CharSequence) {
        viewModelScope.launch(Dispatchers.IO) {
            val data2 = gson.fromJson(
                URL("https://api.opendota.com/api/players/$id/heroes").readText(),
                Array<PlayersHeroStats>::class.java
            )
            _playersHeroStats.postValue(data2.toList())
        }
    }

    fun getHeroNameByIndex(data: List<Hero>, data2: List<PlayersHeroStats>, i: Int): String {
        return data.firstOrNull { v -> v.id == data2[i].hero_id.toInt() }?.localized_name ?: ""
    }
}