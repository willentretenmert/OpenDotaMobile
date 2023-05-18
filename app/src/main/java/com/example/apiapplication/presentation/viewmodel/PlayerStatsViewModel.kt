package com.example.apiapplication.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apiapplication.data.api.OpenDotaAPI
import com.example.apiapplication.data.api.RaspberryAPI
import com.example.apiapplication.data.models.DotaUserRaspberry
import com.example.apiapplication.data.models.Hero
import com.example.apiapplication.data.models.PlayersHeroStats
import com.example.apiapplication.data.models.PlayersProfile
import com.example.apiapplication.data.models.PlayersWinrate
import com.example.apiapplication.data.models.RecentMatches
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class PlayerStatsViewModel : ViewModel() {

    private val _steamIDProfile = MutableStateFlow<DotaUserRaspberry?>(null)
    val steamIDProfile: StateFlow<DotaUserRaspberry?> = _steamIDProfile


    private val _steamComments = MutableStateFlow<List<DotaUserRaspberry.Comment>>(emptyList())
    val steamComments: StateFlow<List<DotaUserRaspberry.Comment>> = _steamComments


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



    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.opendota.com/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val api = retrofit.create(OpenDotaAPI::class.java)

    var gson = GsonBuilder()
        .setLenient()
        .create()

    private val retrofit2 = Retrofit.Builder()
        .baseUrl("http://176.99.158.188:50993/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val api2 = retrofit2.create(RaspberryAPI::class.java)



    fun fetchSteamIDProfile(id: CharSequence) {
        viewModelScope.launch(Dispatchers.IO) {
            val steamIDProfileDeferred = async { api2.getSteamIDProfile("dotaProfile", id) }

            val steamIDProfile = steamIDProfileDeferred.await()

            _steamIDProfile.value = steamIDProfile
            _steamComments.value = steamIDProfile.data
        }
    }

    fun postComment(author: String, content: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentProfile = steamIDProfile.value

            val newComment = DotaUserRaspberry.Comment(content, author)

            val updatedData =
                currentProfile?.data?.toMutableList()?.apply { add(newComment) } ?: listOf(
                    newComment
                )

            val updatedProfile = currentProfile?.copy(data = updatedData)

            val response = api2.postSteamIDProfile(updatedProfile!!)

            val jsonString = response.body().toString()
            Log.d("zxc", jsonString)

            if (response.isSuccessful) {
                Log.d("zxc3", "success")
                _steamIDProfile.value = response.body()
                _steamComments.value = response.body()?.data ?: emptyList()
            } else {
                Log.d("zxc3", "response not successful")
            }
        }
    }

    // gets a json string of dota heroes
    fun fetchHeroes() {
        viewModelScope.launch(Dispatchers.IO) {
            val heroesDeferred = async { api.getHeroes() }

            val heroes = heroesDeferred.await()

            _heroes.value = heroes.toList()
        }
    }

    // gets a json string of player's matches recent
    fun fetchRecentMatches(id: CharSequence) {
        viewModelScope.launch(Dispatchers.IO) {
            val recentMatchesDeferred = async { api.getRecentMatches(id) }

            val recentMatches = recentMatchesDeferred.await()

            _recentMatches.value = recentMatches.toList()
        }
    }

    // gets a json string and makes it a PlayersHeroStats object
    fun fetchPlayerStats(id: CharSequence) {
        viewModelScope.launch(Dispatchers.IO) {
            val playerHeroStatsDeferred = async { api.getPlayerHeroStats(id) }
            val playerProfileDeferred = async { api.getPlayerProfile(id) }
            val playerWinrateDeferred = async { api.getPlayerWinrate(id) }

            val playerHeroStats = playerHeroStatsDeferred.await()
            val playerProfile = playerProfileDeferred.await()
            val playerWinrate = playerWinrateDeferred.await()

            _playersHeroStats.value = playerHeroStats.toList()
            _playersProfile.value = playerProfile
            _playersWinrate.value = playerWinrate
        }
    }


    // returns hero original name requiring a hero list and player's personal statistics list
    fun getHeroNameByPlayerStatsIndex(
        data: List<Hero>,
        data2: List<PlayersHeroStats>,
        i: Int
    ): String {
        val name = data.firstOrNull { v -> v.id == data2[i].hero_id }?.name ?: ""
        return "ic_${name}"
    }

    fun getHeroGames(data: List<PlayersHeroStats>, i: Int): String {
        return data[i].games.toString()
    }

    fun getHeroWinrate(data: List<PlayersHeroStats>, i: Int): String {
        val wins: Int = data[i].win
        val total: Int = data[i].games
        return if (total != 0) (wins.toDouble() / total * 100).toInt().toString() + "%" else "0%"
    }

    // returns player's steam current nickname
    fun getPlayersPersonaName(data: PlayersProfile?): String {
        return data?.profile?.personaname ?: ""
    }

    // returns player's steam profile picture
    fun getPlayersAvatar(data: PlayersProfile?): String {
        return data?.profile?.avatarmedium ?: ""
    }

    // returns player's winrate percentage
    fun getPlayersWinrate(data: PlayersWinrate?): Int {
        val wins: Int = data?.win ?: 0
        val total: Int = wins + (data?.lose ?: 0)
        return if (total != 0) (wins.toDouble() / total * 100).toInt() else 0
    }

    // returns player's total matches count
    fun getPlayersTotal(data: PlayersWinrate?): String {
        return ((data?.win ?: 0) + (data?.lose ?: 0)).toString()
    }

    fun getPlayersRank(data: PlayersProfile?): String {
        return "ic_rank${data?.rank_tier}"
    }

}
