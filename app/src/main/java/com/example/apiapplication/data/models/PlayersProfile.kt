package com.example.apiapplication.data.models

import com.google.gson.annotations.SerializedName

data class PlayersProfile(
    @field:SerializedName("rank_tier") val rank_tier: Int,

    @field:SerializedName("profile") var profile: Profile?,


) {
    data class Profile(
        @field:SerializedName("account_id") val account_id: Int,

        @field:SerializedName("avatar") val avatar: String,

        @field:SerializedName("avatarfull") val avatarfull: String,

        @field:SerializedName("avatarmedium") val avatarmedium: String,

        @field:SerializedName("cheese") val cheese: Int,

        @field:SerializedName("personaname") val personaname: String,
    )
}