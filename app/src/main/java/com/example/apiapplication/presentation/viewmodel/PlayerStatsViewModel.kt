package com.example.apiapplication.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apiapplication.data.Hero
import com.example.apiapplication.data.PlayersWinrate
import com.example.apiapplication.data.PlayersHeroStats
import com.example.apiapplication.data.PlayersProfile
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.net.URL

class PlayerStatsViewModel : ViewModel() {

    private val gson = Gson()

    private val _heroes = MutableStateFlow<List<Hero>>(emptyList())
    val heroes: StateFlow<List<Hero>> = _heroes

    private val _playersHeroStats = MutableStateFlow<List<PlayersHeroStats>>(emptyList())
    val playersHeroStats: StateFlow<List<PlayersHeroStats>> = _playersHeroStats

    private val _playersProfile = MutableStateFlow<PlayersProfile?>(null)
    val playersProfile: StateFlow<PlayersProfile?> = _playersProfile

    private val _playersWinrate = MutableStateFlow<PlayersWinrate?>(null)
    val playersWinrate: StateFlow<PlayersWinrate?> = _playersWinrate

    fun fetchHeroes() {
        viewModelScope.launch(Dispatchers.IO) {
            val data = gson.fromJson(
                URL("https://api.opendota.com/api/heroes").readText(),
                Array<Hero>::class.java
            )
            _heroes.value = data.toList()
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
            val data4 = gson.fromJson(
                URL("https://api.opendota.com/api/players/$id/wl").readText(),
                PlayersWinrate::class.java
            )
            _playersHeroStats.value = data2.toList()
            _playersProfile.value = data3
            _playersWinrate.value = data4
        }
    }

    // returns hero localized name requiring a hero list and player's personal statistics list
    fun getHeroNameByIndex(data: List<Hero>, data2: List<PlayersHeroStats>, i: Int): String {
        return data.firstOrNull { v -> v.id == data2[i].hero_id.toInt() }?.localized_name ?: ""
    }

    fun getPlayersPersonaName(data3: PlayersProfile?): String {
        return data3?.profile?.personaname ?: ""
    }
    fun getPlayersWinrate(data4: PlayersWinrate?): String {
        val wins: Int = data4?.win ?: 0
        val total: Int = wins + (data4?.lose ?: 0)
        return if (total != 0) (wins.toDouble() / total * 100).toInt().toString() + "%" else "0%"
    }

    fun getPlayersTotal(data4: PlayersWinrate?): String {
        return ((data4?.win ?: 0) + (data4?.lose ?: 0)).toString()
    }
}
