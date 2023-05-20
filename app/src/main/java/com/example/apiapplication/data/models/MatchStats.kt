package com.example.apiapplication.data.models

import com.google.gson.annotations.SerializedName

data class MatchStats(
    @field:SerializedName("duration") val duration: Int,

    @field:SerializedName("game_mode") val game_mode: Int,

    @field:SerializedName("match_id") val match_id: Long,

    @field:SerializedName("players") val players: List<Player>,

    @field:SerializedName("radiant_win") val radiant_win: Boolean,

    @field:SerializedName("start_time") val start_time: Long,
) {
    data class Player(
        @field:SerializedName("account_id") val account_id: Int,

        @field:SerializedName("assists") val assists: Int,

        @field:SerializedName("deaths") val deaths: Int,

        @field:SerializedName("hero_id") val hero_id: Int,

        @field:SerializedName("item_0") val item_0: Int,

        @field:SerializedName("item_1") val item_1: Int,

        @field:SerializedName("item_2") val item_2: Int,

        @field:SerializedName("item_3") val item_3: Int,

        @field:SerializedName("item_4") val item_4: Int,

        @field:SerializedName("item_5") val item_5: Int,

        @field:SerializedName("kills") val kills: Int,

        @field:SerializedName("name") val name: Any,

        @field:SerializedName("net_worth") val net_worth: Int,

        @field:SerializedName("player_slot") val player_slot: Int,
    )
}


