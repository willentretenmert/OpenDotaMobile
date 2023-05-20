package com.example.apiapplication.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.apiapplication.data.api.OpenDotaAPI
import com.example.apiapplication.data.models.DotaUserRaspberry
import com.example.apiapplication.data.models.FirebaseUserRaspberry
import com.example.apiapplication.data.models.Hero
import com.example.apiapplication.data.models.MatchStats
import com.example.apiapplication.data.models.PlayersHeroStats
import com.example.apiapplication.data.models.PlayersProfile
import com.example.apiapplication.data.models.PlayersWinrate
import com.example.apiapplication.data.models.RecentMatches
import com.example.apiapplication.networking.OpenDotaApiProvider
import com.example.apiapplication.networking.RaspberryPiProvider
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FavouritesViewModel : ViewModel() {

    val apiProvider = OpenDotaApiProvider()
    val raspberryPiProvider = RaspberryPiProvider()

    val heroes: StateFlow<List<Hero>> = apiProvider.heroes
    private val _playersHeroStats = MutableStateFlow<List<PlayersHeroStats>>(emptyList())
    val playersHeroStats: StateFlow<List<PlayersHeroStats>> = _playersHeroStats

    private val _playersProfile = MutableStateFlow<PlayersProfile?>(null)
    val playersProfile: StateFlow<PlayersProfile?> = _playersProfile

    private val _playersWinrate = MutableStateFlow<PlayersWinrate?>(null)
    val playersWinrate: StateFlow<PlayersWinrate?> = _playersWinrate
    private val _matchStats = MutableStateFlow<MatchStats?>(null)
    val matchStats: StateFlow<MatchStats?> = _matchStats

    private val _players = MutableStateFlow<List<MatchStats.Player>>(emptyList())
    val players: StateFlow<List<MatchStats.Player>> = _players

    val firebaseProfile: StateFlow<FirebaseUserRaspberry?> = raspberryPiProvider.firebaseProfile
    val favouritesPlayers: StateFlow<List<FirebaseUserRaspberry.FavouritePlayers>> =
        raspberryPiProvider.favouritesPlayers
    val favouritesMatches: StateFlow<List<FirebaseUserRaspberry.FavouriteMatches>> =
        raspberryPiProvider.favouritesMatches

    // http://176.99.158.188:50993/request?key=dotaProfile&id=275690206
    // http://176.99.158.188:50993/request?key=firebaseProfile&id=12345

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.opendota.com/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(OpenDotaAPI::class.java)

    // gets a json string of dota heroes
    fun fetchHeroes() {
        apiProvider.fetchHeroes()
    }

    fun deleteFavouritePlayer(
        userMail: String,
        nickname: String,
        playerId: String,
        callback: (Boolean) -> Unit
    ) {
        raspberryPiProvider.deleteFavouritePlayer(userMail, nickname, playerId, callback)
    }
    fun deleteFavouriteMatch(
        userMail: String,
        nickname: String,
        matchId: String,
        callback: (Boolean) -> Unit
    ) {
        raspberryPiProvider.deleteFavouriteMatch(userMail, nickname, matchId, callback)
    }

    // gets a json string and makes it a PlayersHeroStats object
    fun fetchPlayerStats(id: CharSequence): Flow<Unit> = flow {
        coroutineScope {
            val playerHeroStatsDeferred = async { api.getPlayerHeroStats(id) }
            val playerProfileDeferred = async { api.getPlayerProfile(id) }
            val playerWinrateDeferred = async { api.getPlayerWinrate(id) }

            _playersHeroStats.value = playerHeroStatsDeferred.await().toList()
            _playersProfile.value = playerProfileDeferred.await()
            _playersWinrate.value = playerWinrateDeferred.await()

            emit(Unit)
        }
    }


    fun fetchMatchStats(id: CharSequence): Flow<Unit> = flow {
        coroutineScope {
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
                    emit(Unit)
                }
            } catch (e: Exception) {
                Log.e("zxc", "Error fetching match stats: ${e.message}")
            }
        }
    }

    fun fetchFirebaseProfile(mail: CharSequence) {
        raspberryPiProvider.fetchFirebaseProfile(mail)
    }


}