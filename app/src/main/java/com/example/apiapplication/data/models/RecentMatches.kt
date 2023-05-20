package com.example.apiapplication.data.models

import com.google.gson.annotations.SerializedName

data class RecentMatches(
    @field:SerializedName("assists")
    val assists: Int,
    @field:SerializedName("deaths")
    val deaths: Int,
    @field:SerializedName("duration")
    val duration: Int,
    @field:SerializedName("game_mode")
    val game_mode: Int,
    @field:SerializedName("hero_id")
    val hero_id: Int,
    @field:SerializedName("kills")
    val kills: Int,
    @field:SerializedName("player_slot")
    val player_slot: Int,
    @field:SerializedName("match_id")
    val match_id: Long,
    @field:SerializedName("radiant_win")
    val radiant_win: Boolean,
    @field:SerializedName("start_time")
    val start_time: Int,
)