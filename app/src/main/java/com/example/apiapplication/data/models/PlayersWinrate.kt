package com.example.apiapplication.data.models

import com.google.gson.annotations.SerializedName


data class PlayersWinrate(
    @field:SerializedName("win")
    val win: Int,
    @field:SerializedName("lose")
    val lose: Int
)