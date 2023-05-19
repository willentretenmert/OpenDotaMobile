package com.example.apiapplication.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apiapplication.data.api.OpenDotaAPI
import com.example.apiapplication.data.api.RaspberryAPI
import com.example.apiapplication.data.models.FirebaseUserRaspberry
import com.example.apiapplication.data.models.Hero
import com.example.apiapplication.data.models.MatchStats
import com.example.apiapplication.data.models.PlayersHeroStats
import com.example.apiapplication.data.models.PlayersProfile
import com.example.apiapplication.data.models.PlayersWinrate
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

class FavouritesViewModel : ViewModel() {

    private val _heroes = MutableStateFlow<List<Hero>>(emptyList())
    val heroes: StateFlow<List<Hero>> = _heroes

    private val _playersHeroStats = MutableStateFlow<List<PlayersHeroStats>>(emptyList())
    val playersHeroStats: StateFlow<List<PlayersHeroStats>> = _playersHeroStats

    private val _playersProfile = MutableStateFlow<PlayersProfile?>(null)
    val playersProfile: StateFlow<PlayersProfile?> = _playersProfile

    private val _playersWinrate = MutableStateFlow<PlayersWinrate?>(null)
    val playersWinrate: StateFlow<PlayersWinrate?> = _playersWinrate

    private val _matchStats = MutableStateFlow<MatchStats?>(null)
    val matchStats: StateFlow<MatchStats?> = _matchStats

    private val _firebaseProfile = MutableStateFlow<FirebaseUserRaspberry?>(null)
    val firebaseProfile: StateFlow<FirebaseUserRaspberry?> = _firebaseProfile

    private val _favouritesPlayers = MutableStateFlow<List<FirebaseUserRaspberry.FavouritePlayers>>(emptyList())
    val favouritesPlayers: StateFlow<List<FirebaseUserRaspberry.FavouritePlayers>> = _favouritesPlayers

    private val _favouritesMatches = MutableStateFlow<List<FirebaseUserRaspberry.FavouriteMatches>>(emptyList())
    val favouritesMatches: StateFlow<List<FirebaseUserRaspberry.FavouriteMatches>> = _favouritesMatches

    // http://176.99.158.188:50993/request?key=dotaProfile&id=275690206
    // http://176.99.158.188:50993/request?key=firebaseProfile&id=12345


    private val retrofitRaspberryAPI = Retrofit.Builder()
        .baseUrl("http://176.99.158.188:50993/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retrofitOpenDotaAPI = Retrofit.Builder()
        .baseUrl("https://api.opendota.com/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiRaspberryAPI = retrofitRaspberryAPI.create(RaspberryAPI::class.java)

    private val apiOpenDotaAPI = retrofitOpenDotaAPI.create(OpenDotaAPI::class.java)

    fun fetchHeroes() {
        viewModelScope.launch(Dispatchers.IO) {
            val heroesDeferred = async { apiOpenDotaAPI.getHeroes() }
            val heroes = heroesDeferred.await()

            _heroes.value = heroes.toList()
        }
    }

    fun fetchPlayerStats(id: CharSequence) {
        viewModelScope.launch(Dispatchers.IO) {
            val playerHeroStatsDeferred = async { apiOpenDotaAPI.getPlayerHeroStats(id) }
            val playerProfileDeferred = async { apiOpenDotaAPI.getPlayerProfile(id) }
            val playerWinrateDeferred = async { apiOpenDotaAPI.getPlayerWinrate(id) }

            val playerHeroStats = playerHeroStatsDeferred.await()
            val playerProfile = playerProfileDeferred.await()
            val playerWinrate = playerWinrateDeferred.await()

            _playersHeroStats.value = playerHeroStats.toList()
            _playersProfile.value = playerProfile
            _playersWinrate.value = playerWinrate
        }
    }

    fun fetchMatchStats(id: CharSequence) {
        viewModelScope.launch(Dispatchers.IO) {
            val matchStatsDeferred = async { apiOpenDotaAPI.getMatch(id) }
            val matchStats = matchStatsDeferred.await()

            _matchStats.value = matchStats
        }
    }

    fun fetchFirebaseProfile(id: CharSequence) {
        viewModelScope.launch(Dispatchers.IO) {
            val firebaseProfileDeferred = async { apiRaspberryAPI.getFirebaseProfile(key = "firebaseProfile", id = id) }
            val firebaseProfile = firebaseProfileDeferred.await()

            _firebaseProfile.value = firebaseProfile
            _favouritesPlayers.value = firebaseProfile.players
            _favouritesMatches.value = firebaseProfile.matches
        }
    }

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

    fun getFirebaseFavouritesPlayers(data: FirebaseUserRaspberry?) : List<FirebaseUserRaspberry.FavouritePlayers>? {
        return data?.players
    }

    fun getFirebaseFavouritesMatches(data: FirebaseUserRaspberry?) : List<FirebaseUserRaspberry.FavouriteMatches>? {
        return data?.matches
    }
}