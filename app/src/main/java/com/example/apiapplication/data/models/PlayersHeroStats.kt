package com.example.apiapplication.data.models

import kotlinx.serialization.*
import com.google.gson.annotations.SerializedName

data class PlayersHeroStats(
    @field:SerializedName("games")
    val games: Int,
    @field:SerializedName("hero_id")
    val hero_id: Int,
    @field:SerializedName("win")
    val win: Int,
)