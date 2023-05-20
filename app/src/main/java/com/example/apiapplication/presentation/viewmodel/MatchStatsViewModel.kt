package com.example.apiapplication.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.apiapplication.data.models.FirebaseUserRaspberry
import com.example.apiapplication.data.models.Hero
import com.example.apiapplication.data.models.MatchStats
import com.example.apiapplication.networking.OpenDotaApiProvider
import com.example.apiapplication.networking.RaspberryPiProvider
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MatchStatsViewModel : ViewModel() {

    val apiProvider = OpenDotaApiProvider()
    val raspberryPiProvider = RaspberryPiProvider()

    val heroes: StateFlow<List<Hero>> = apiProvider.heroes
    val matchStats: StateFlow<MatchStats?> = apiProvider.matchStats
    val players: StateFlow<List<MatchStats.Player>> = apiProvider.players
    val favouritesMatches: StateFlow<List<FirebaseUserRaspberry.FavouriteMatches>> =
        raspberryPiProvider.favouritesMatches

    fun postFavouriteMatch(
        userMail: String,
        nickname: String,
        matchId: String,
        callback: (Boolean) -> Unit
    ) {
        raspberryPiProvider.postFavouriteMatch(
            userMail,
            nickname,
            matchId,
            callback
        )
    }
    fun deleteFavouriteMatch(
        userMail: String,
        nickname: String,
        matchId: String,
        callback: (Boolean) -> Unit
    ) {
        raspberryPiProvider.deleteFavouriteMatch(
            userMail,
            nickname,
            matchId,
            callback
        )
    }

    fun fetchHeroes() {
        apiProvider.fetchHeroes()
    }

    fun fetchMatchStats(id: CharSequence) {
        apiProvider.fetchMatchStats(id)
    }

    fun getMatchStartDate(data: MatchStats?): String {
        val date = Date((data?.start_time ?: 0) * 1000L)
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
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