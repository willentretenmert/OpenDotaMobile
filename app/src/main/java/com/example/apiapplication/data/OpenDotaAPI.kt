package com.example.apiapplication.data

import retrofit2.http.GET
import retrofit2.http.Path

interface OpenDotaAPI {
    @GET("players/{id}/heroes")
    suspend fun getPlayerHeroStats(@Path("id") id: CharSequence): Array<PlayersHeroStats>

    @GET("players/{id}/")
    suspend fun getPlayerProfile(@Path("id") id: CharSequence): PlayersProfile

    @GET("players/{id}/wl")
    suspend fun getPlayerWinrate(@Path("id") id: CharSequence): PlayersWinrate
}