package com.example.apiapplication.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.apiapplication.data.models.DotaUserRaspberry
import com.example.apiapplication.data.models.Hero
import com.example.apiapplication.data.models.MatchStats
import com.example.apiapplication.data.models.PlayersHeroStats
import com.example.apiapplication.data.models.PlayersProfile
import com.example.apiapplication.data.models.PlayersWinrate
import com.example.apiapplication.data.models.RecentMatches
import com.example.apiapplication.networking.OpenDotaApiProvider
import com.example.apiapplication.networking.RaspberryPiProvider
import kotlinx.coroutines.flow.StateFlow


class PlayerStatsViewModel : ViewModel() {

    val apiProvider = OpenDotaApiProvider()
    val raspberryPiProvider = RaspberryPiProvider()

    val heroes: StateFlow<List<Hero>> = apiProvider.heroes
    val playersHeroStats: StateFlow<List<PlayersHeroStats>> = apiProvider.playersHeroStats
    val playersProfile: StateFlow<PlayersProfile?> = apiProvider.playersProfile
    val playersWinRate: StateFlow<PlayersWinrate?> = apiProvider.playersWinrate
    val recentMatches: StateFlow<List<RecentMatches>> = apiProvider.recentMatches
    val matchStats: StateFlow<MatchStats?> = raspberryPiProvider.matchStats
    val players: StateFlow<List<MatchStats.Player>> = raspberryPiProvider.players
    val steamIDProfile: StateFlow<DotaUserRaspberry?> = raspberryPiProvider.steamIDProfile
    val steamComments: StateFlow<List<DotaUserRaspberry.Comment>> =
        raspberryPiProvider.steamComments

    fun fetchSteamIDProfile(id: CharSequence) {
        raspberryPiProvider.fetchSteamIDProfile(id)
    }
    fun fetchFirebaseProfile(mail: CharSequence) {
        raspberryPiProvider.fetchFirebaseProfile(mail)
    }

    fun postComment(id: String, author: String, content: String, callback: (Boolean) -> Unit) {
        raspberryPiProvider.postComment(id, author, content, callback)
    }

    fun postFavouritePlayer(
        userMail: String,
        nickname: String,
        playerId: String,
        callback: (Boolean) -> Unit
    ) {
        raspberryPiProvider.postFavouritePlayer(userMail, nickname, playerId, callback)
    }

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
