package com.example.apiapplication.data.models

import kotlinx.serialization.*
import com.google.gson.annotations.SerializedName

data class PlayersHeroStats(
    @field:SerializedName("against_games")
    val against_games: Int,
    @field:SerializedName("against_win")
    val against_win: Int,
    @field:SerializedName("games")
    val games: Int,
    @field:SerializedName("hero_id")
    val hero_id: Int,
    @field:SerializedName("last_played")
    val last_played: Int,
    @field:SerializedName("win")
    val win: Int,
    @field:SerializedName("with_games")
    val with_games: Int,
    @field:SerializedName("with_win")
    val with_win: Int,
    @field:SerializedName("parents")
    val parents: List<String?>? = null
)