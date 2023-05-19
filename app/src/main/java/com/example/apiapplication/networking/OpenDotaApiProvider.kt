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
import retrofit2.HttpException
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
            try {
                val heroesDeferred = async {
                    try {
                        api.getHeroes()
                    } catch (e: Exception) {
                        Log.e("zxc", "Error fetching heroes: ${e.message}")
                        return@async null
                    }
                }
                val heroes = heroesDeferred.await()

                if (heroes != null) {
                    _heroes.value = heroes.toList()
                }
            } catch (e: Exception) {
                Log.e("zxc", "Error fetching match stats: ${e.message}")
            }
        }
    }

    fun fetchRecentMatches(id: CharSequence) {
        coroutineScope.launch(Dispatchers.IO) {
            try {
                val recentMatchesDeferred = async {
                    try {
                        api.getRecentMatches(id)
                    } catch (e: Exception) {
                        Log.e("zxc", "Error fetching recent matches: ${e.message}")
                        return@async null
                    }
                }
                val recentMatches = recentMatchesDeferred.await()

                if (recentMatches != null) {
                    _recentMatches.value = recentMatches.toList()
                }
            } catch (e: Exception) {
                Log.e("zxc", "Error fetching match stats: ${e.message}")
            }
        }
    }

    fun fetchPlayerStats(id: CharSequence) {
        coroutineScope.launch(Dispatchers.IO) {
            try {
                val playerHeroStatsDeferred = async {
                    try {
                        api.getPlayerHeroStats(id)
                    } catch (e: Exception) {
                        Log.e("zxc", "Error fetching player hero stats: ${e.message}")
                        return@async null
                    }
                }
                val playerProfileDeferred = async {
                    try {
                        api.getPlayerProfile(id)
                    } catch (e: Exception) {
                        Log.e("zxc", "Error fetching player profile: ${e.message}")
                        return@async null
                    }
                }
                val playerWinrateDeferred = async {
                    try {
                        api.getPlayerWinrate(id)
                    } catch (e: Exception) {
                        Log.e("zxc", "Error fetching player winrate: ${e.message}")
                        return@async null
                    }
                }

                val playerHeroStats = playerHeroStatsDeferred.await()
                val playerProfile = playerProfileDeferred.await()
                val playerWinrate = playerWinrateDeferred.await()

                if (playerHeroStats != null) {
                    _playersHeroStats.value = playerHeroStats.toList()
                }
                if (playerProfile != null) {
                    _playersProfile.value = playerProfile
                }
                if (playerWinrate != null) {
                    _playersWinrate.value = playerWinrate
                }
            } catch (e: Exception) {
                Log.e("zxc", "Error fetching match stats: ${e.message}")
            }
        }
    }

    fun fetchMatchStats(id: CharSequence) {
        coroutineScope.launch(Dispatchers.IO) {
            try {
                val matchStatsDeferred = async {
                    try {
                        api.getMatch(id)
                    } catch (e: Exception) {
                        Log.d("zxc", "error 404")
                        return@async null
                    }
                }
                val matchStats = matchStatsDeferred.await()

                if (matchStats != null) {
                    _matchStats.value = matchStats
                    _players.value = matchStats.players
                }
            } catch (e: Exception) {
                Log.e("zxc", "Error fetching match stats: ${e.message}")
            }
        }
    }
}