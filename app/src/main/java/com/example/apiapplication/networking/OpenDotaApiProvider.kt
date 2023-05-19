package com.example.apiapplication.networking

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.apiapplication.data.api.OpenDotaAPI
import com.example.apiapplication.data.api.RaspberryAPI
import com.example.apiapplication.data.models.DotaUserRaspberry
import com.example.apiapplication.data.models.Hero
import com.example.apiapplication.data.models.MatchStats
import com.example.apiapplication.data.models.PlayersHeroStats
import com.example.apiapplication.data.models.PlayersProfile
import com.example.apiapplication.data.models.PlayersWinrate
import com.example.apiapplication.data.models.RecentMatches
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OpenDotaApiProvider {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val _heroes = MutableStateFlow<List<Hero>>(emptyList())
    val heroes: StateFlow<List<Hero>> = _heroes

    private val _playersHeroStats = MutableStateFlow<List<PlayersHeroStats>>(emptyList())
    val playersHeroStats: StateFlow<List<PlayersHeroStats>> = _playersHeroStats

    private val _playersProfile = MutableStateFlow<PlayersProfile?>(null)
    val playersProfile: StateFlow<PlayersProfile?> = _playersProfile

    private val _playersWinrate = MutableStateFlow<PlayersWinrate?>(null)
    val playersWinrate: StateFlow<PlayersWinrate?> = _playersWinrate

    private val _recentMatches = MutableStateFlow<List<RecentMatches>>(emptyList())
    val recentMatches: StateFlow<List<RecentMatches>> = _recentMatches

    private val _matchStats = MutableStateFlow<MatchStats?>(null)
    val matchStats: StateFlow<MatchStats?> = _matchStats

    private val _players = MutableStateFlow<List<MatchStats.Player>>(emptyList())
    val players: StateFlow<List<MatchStats.Player>> = _players

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.opendota.com/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val api = retrofit.create(OpenDotaAPI::class.java)

    var gson = GsonBuilder().setLenient().create()

    // gets a json string of dota heroes
    fun fetchHeroes() {
        coroutineScope.launch(Dispatchers.IO) {
            val heroesDeferred = async { api.getHeroes() }

            val heroes = heroesDeferred.await()

            _heroes.value = heroes.toList()
        }
    }

    // gets a json string of player's matches recent
    fun fetchRecentMatches(id: CharSequence) {
        coroutineScope.launch(Dispatchers.IO) {
            val recentMatchesDeferred = async { api.getRecentMatches(id) }

            val recentMatches = recentMatchesDeferred.await()

            _recentMatches.value = recentMatches.toList()
        }
    }

    // gets a json string and makes it a PlayersHeroStats object
    fun fetchPlayerStats(id: CharSequence) {
        coroutineScope.launch(Dispatchers.IO) {
            val playerHeroStatsDeferred = async { api.getPlayerHeroStats(id) }
            val playerProfileDeferred = async { api.getPlayerProfile(id) }
            val playerWinrateDeferred = async { api.getPlayerWinrate(id) }

            _playersHeroStats.value = playerHeroStatsDeferred.await().toList()
            _playersProfile.value = playerProfileDeferred.await()
            _playersWinrate.value = playerWinrateDeferred.await()
        }
    }

    fun fetchMatchStats(id: CharSequence) {
        coroutineScope.launch(Dispatchers.IO) {
            val matchStatsDeferred = async { api.getMatch(id) }
            val matchStats = matchStatsDeferred.await()

            _matchStats.value = matchStats
            _players.value = matchStats.players
        }
    }
}