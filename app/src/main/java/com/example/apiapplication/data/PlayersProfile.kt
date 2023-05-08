package com.example.apiapplication.data

import com.google.gson.annotations.SerializedName

data class PlayersProfile(
    @field:SerializedName("competitive_rank")
    val competitive_rank: Any,
    @field:SerializedName("leaderboard_rank")
    val leaderboard_rank: Any,
    @field:SerializedName("rank_tier")
    val rank_tier: Int,
    @field:SerializedName("solo_competitive_rank")
    val solo_competitive_rank: Any,
    @field:SerializedName("profile")
    var profile: List<Profile?>?,
    @field:SerializedName("mmr_estimate")
    var mmr_estimate: List<MMREstimate?>?
)