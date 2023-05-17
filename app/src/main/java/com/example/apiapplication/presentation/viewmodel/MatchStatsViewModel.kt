package com.example.apiapplication.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apiapplication.data.Hero
import com.example.apiapplication.data.MatchStats
import com.example.apiapplication.data.OpenDotaAPI
import com.example.apiapplication.data.RecentMatches
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MatchStatsViewModel : ViewModel() {

    private val _heroes = MutableStateFlow<List<Hero>>(emptyList())
    val heroes: StateFlow<List<Hero>> = _heroes

    private val _matchStats = MutableStateFlow<MatchStats?>(null)
    val matchStats: StateFlow<MatchStats?> = _matchStats

    private val _players = MutableStateFlow<List<MatchStats.Player>>(emptyList())
    val players: StateFlow<List<MatchStats.Player>> = _players


    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.opendota.com/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(OpenDotaAPI::class.java)

    fun fetchHeroes() {
        viewModelScope.launch(Dispatchers.IO) {
            val heroesDeferred = async { api.getHeroes() }
            val heroes = heroesDeferred.await()

            _heroes.value = heroes.toList()
        }
    }

    fun fetchMatchStats(id: CharSequence) {
        viewModelScope.launch(Dispatchers.IO) {
            val matchStatsDeferred = async { api.getMatch(id) }
            val matchStats = matchStatsDeferred.await()

            _matchStats.value = matchStats
            _players.value = matchStats.players
        }
    }

    fun getMatchStartDate(data: MatchStats?): String {
        val date = Date((data?.start_time ?: 0) * 1000L)
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        Log.d("zxc", dateFormat.format(date))
        return dateFormat.format(date)
    }

    fun getMatchDurationHMS(data: MatchStats?): String {
        val hours = (data?.duration ?: 0) / 3600
        val minutes = ((data?.duration ?: 0) % 3600) / 60
        val seconds = (data?.duration ?: 0) % 60
        return if (hours > 0) String.format("%d:%02d:%02d", hours, minutes, seconds)
        else String.format("%02d:%02d", minutes, seconds)
    }

    fun getOutcome(data: MatchStats?): Boolean? {
        return data?.radiant_win
    }


}