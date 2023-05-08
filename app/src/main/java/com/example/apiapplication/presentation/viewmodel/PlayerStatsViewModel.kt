package com.example.apiapplication.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apiapplication.data.Hero
import com.example.apiapplication.data.PlayersHeroStats
import com.example.apiapplication.data.PlayersProfile
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL

class PlayerStatsViewModel : ViewModel() {

    private val gson = Gson()

    private val _heroes = MutableLiveData<List<Hero>>()
    val heroes: LiveData<List<Hero>> = _heroes

    private val _playersHeroStats = MutableLiveData<List<PlayersHeroStats>>()
    val playersHeroStats: LiveData<List<PlayersHeroStats>> = _playersHeroStats

    private val _playersProfile = MutableLiveData<PlayersProfile>()
    val playersProfile: LiveData<PlayersProfile> = _playersProfile

    fun fetchHeroes() {
        viewModelScope.launch(Dispatchers.IO) {
            val data = gson.fromJson(
                URL("https://api.opendota.com/api/heroes").readText(),
                Array<Hero>::class.java
            )
            _heroes.postValue(data.toList())
        }
    }

    // gets a json string and makes it a PlayersHeroStats object
    fun fetchPlayerStats(id: CharSequence) {
        viewModelScope.launch(Dispatchers.IO) {
            val data2 = gson.fromJson(
                URL("https://api.opendota.com/api/players/$id/heroes").readText(),
                Array<PlayersHeroStats>::class.java
            )
            val data3 = gson.fromJson(
                URL("https://api.opendota.com/api/players/$id/").readText(),
                PlayersProfile::class.java
            )
            _playersHeroStats.postValue(data2.toList())
            _playersProfile.postValue(data3)
        }
    }


    // returns hero localized name requiring a hero list and player's personal statistics list
    fun getHeroNameByIndex(data: List<Hero>, data2: List<PlayersHeroStats>, i: Int): String {
        return data.firstOrNull { v -> v.id == data2[i].hero_id.toInt() }?.localized_name ?: ""
    }

    fun getPlayersPersonaName(data3: PlayersProfile): String {
        return data3.profile?.firstOrNull()!!.personaname
    }

}
