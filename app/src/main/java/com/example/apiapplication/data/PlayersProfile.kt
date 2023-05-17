package com.example.apiapplication.data

import com.google.gson.annotations.SerializedName

data class PlayersProfile(
    @field:SerializedName("competitive_rank") val competitive_rank: Any,

    @field:SerializedName("leaderboard_rank") val leaderboard_rank: Any,

    @field:SerializedName("rank_tier") val rank_tier: Int,

    @field:SerializedName("solo_competitive_rank") val solo_competitive_rank: Any,

    @field:SerializedName("profile") var profile: Profile?,

    @field:SerializedName("mmr_estimate") var mmr_estimate: MMREstimate?

) {
    data class Profile(
        @field:SerializedName("account_id") val account_id: Int,

        @field:SerializedName("avatar") val avatar: String,

        @field:SerializedName("avatarfull") val avatarfull: String,

        @field:SerializedName("avatarmedium") val avatarmedium: String,

        @field:SerializedName("cheese") val cheese: Int,

        @field:SerializedName("is_contributor") val is_contributor: Boolean,

        @field:SerializedName("is_subscriber") val is_subscriber: Boolean,

        @field:SerializedName("last_login") val last_login: String,

        @field:SerializedName("loccountrycode") val loccountrycode: String,

        @field:SerializedName("name") val name: Any,

        @field:SerializedName("personaname") val personaname: String,

        @field:SerializedName("plus") val plus: Boolean,

        @field:SerializedName("profileurl") val profileurl: String,

        @field:SerializedName("status") val status: Any,

        @field:SerializedName("steamid") val steamid: String
    )

    data class MMREstimate(
        @field:SerializedName("estimate") val estimate: Int
    )
}