package com.example.apiapplication.data.api

import com.example.apiapplication.data.models.Hero
import com.example.apiapplication.data.models.MatchStats
import com.example.apiapplication.data.models.PlayersHeroStats
import com.example.apiapplication.data.models.PlayersProfile
import com.example.apiapplication.data.models.PlayersWinrate
import com.example.apiapplication.data.models.RecentMatches
import retrofit2.http.GET
import retrofit2.http.Path

interface OpenDotaAPI {
    // getting heroes json
    @GET("heroes")
    suspend fun getHeroes(): Array<Hero>


    // getting player stats json
    @GET("players/{id}/")
    suspend fun getPlayerProfile(@Path("id") id: CharSequence): PlayersProfile

    @GET("players/{id}/heroes")
    suspend fun getPlayerHeroStats(@Path("id") id: CharSequence): Array<PlayersHeroStats>

    @GET("players/{id}/wl")
    suspend fun getPlayerWinrate(@Path("id") id: CharSequence): PlayersWinrate

    @GET("players/{id}/recentMatches")
    suspend fun getRecentMatches(@Path("id") id: CharSequence): Array<RecentMatches>


    // getting match stats json
    @GET("matches/{id}")
    suspend fun getMatch(@Path("id") id: CharSequence): MatchStats
}