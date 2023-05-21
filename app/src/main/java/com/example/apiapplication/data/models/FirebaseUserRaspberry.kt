package com.example.apiapplication.data.models

import com.google.gson.annotations.SerializedName

data class FirebaseUserRaspberry(
    @field:SerializedName("_firebase_id") val _firebase_id: String,

    @field:SerializedName("_name") val _name: String,

    @field:SerializedName("players") val players: List<FavouritePlayers>,

    @field:SerializedName("matches") val matches: List<FavouriteMatches>
) {
    data class FavouritePlayers(
        @field:SerializedName("steam_id") val steam_id: String
    )
    data class FavouriteMatches(
        @field:SerializedName("match_id") val match_id: String
    )
}
