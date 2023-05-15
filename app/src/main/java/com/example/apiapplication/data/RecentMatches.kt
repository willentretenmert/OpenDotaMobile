package com.example.apiapplication.data

import com.google.gson.annotations.SerializedName

data class RecentMatches(
    @field:SerializedName("assists")
    val assists: Int,
    @field:SerializedName("average_rank")
    val average_rank: Int,
    @field:SerializedName("cluster")
    val cluster: Int,
    @field:SerializedName("deaths")
    val deaths: Int,
    @field:SerializedName("duration")
    val duration: Int,
    @field:SerializedName("game_mode")
    val game_mode: Int,
    @field:SerializedName("gold_per_min")
    val gold_per_min: Int,
    @field:SerializedName("hero_damage")
    val hero_damage: Int,
    @field:SerializedName("hero_healing")
    val hero_healing: Int,
    @field:SerializedName("hero_id")
    val hero_id: Int,
    @field:SerializedName("is_roaming")
    val is_roaming: Any,
    @field:SerializedName("kills")
    val kills: Int,
    @field:SerializedName("lane")
    val lane: Any,
    @field:SerializedName("lane_role")
    val lane_role: Any,
    @field:SerializedName("last_hits")
    val last_hits: Int,
    @field:SerializedName("leaver_status")
    val leaver_status: Int,
    @field:SerializedName("lobby_type")
    val lobby_type: Int,
    @field:SerializedName("match_id")
    val match_id: Long,
    @field:SerializedName("party_size")
    val party_size: Int,
    @field:SerializedName("player_slot")
    val player_slot: Int,
    @field:SerializedName("radiant_win")
    val radiant_win: Boolean,
    @field:SerializedName("skill")
    val skill: Any,
    @field:SerializedName("start_time")
    val start_time: Int,
    @field:SerializedName("tower_damage")
    val tower_damage: Int,
    @field:SerializedName("version")
    val version: Any,
    @field:SerializedName("xp_per_min")
    val xp_per_min: Int
)