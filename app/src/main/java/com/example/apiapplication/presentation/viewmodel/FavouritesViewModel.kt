package com.example.apiapplication.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apiapplication.data.api.OpenDotaAPI
import com.example.apiapplication.data.api.RaspberryAPI
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FavouritesViewModel : ViewModel() {

    val apiProvider = OpenDotaApiProvider()
    val raspberryPiProvider = RaspberryPiProvider()

    val heroes: StateFlow<List<Hero>> = apiProvider.heroes
    val playersHeroStats: StateFlow<List<PlayersHeroStats>> = apiProvider.playersHeroStats
    val playersProfile: StateFlow<PlayersProfile?> = apiProvider.playersProfile
    val playersWinrate: StateFlow<PlayersWinrate?> = apiProvider.playersWinrate
    val recentMatches: StateFlow<List<RecentMatches>> = apiProvider.recentMatches
    val matchStats: StateFlow<MatchStats?> = raspberryPiProvider.matchStats
    val players: StateFlow<List<MatchStats.Player>> = raspberryPiProvider.players
    val steamIDProfile: StateFlow<DotaUserRaspberry?> = raspberryPiProvider.steamIDProfile
    val steamComments: StateFlow<List<DotaUserRaspberry.Comment>> =
        raspberryPiProvider.steamComments
    val firebaseProfile: StateFlow<FirebaseUserRaspberry?> = raspberryPiProvider.firebaseProfile
    val favouritesPlayers: StateFlow<List<FirebaseUserRaspberry.FavouritePlayers>> =
        raspberryPiProvider.favouritesPlayers
    val favouritesMatches: StateFlow<List<FirebaseUserRaspberry.FavouriteMatches>> =
        raspberryPiProvider.favouritesMatches

    // http://176.99.158.188:50993/request?key=dotaProfile&id=275690206
    // http://176.99.158.188:50993/request?key=firebaseProfile&id=12345


    // gets a json string of dota heroes
    fun fetchHeroes() {
        apiProvider.fetchHeroes()
    }

    // gets a json string of player's matches recent
    fun fetchRecentMatches(id: CharSequence) {
        apiProvider.fetchRecentMatches(id)
    }

    // gets a json string and makes it a PlayersHeroStats object
    fun fetchPlayerStats(id: CharSequence) {
        apiProvider.fetchPlayerStats(id)
    }


    fun fetchMatchStats(id: CharSequence) {
        apiProvider.fetchMatchStats(id)
    }

    fun fetchFirebaseProfile(mail: CharSequence) {
        raspberryPiProvider.fetchFirebaseProfile(mail)
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

    fun getFirebaseFavouritesPlayers(data: FirebaseUserRaspberry?): List<FirebaseUserRaspberry.FavouritePlayers>? {
        return data?.players
    }

    fun getFirebaseFavouritesMatches(data: FirebaseUserRaspberry?): List<FirebaseUserRaspberry.FavouriteMatches>? {
        return data?.matches
    }
}