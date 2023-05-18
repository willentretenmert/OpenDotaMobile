package com.example.apiapplication.data.models

import com.google.gson.annotations.SerializedName

data class DotaUserRaspberry(

    @field:SerializedName("_steam_id") val _steam_id: String,

    @field:SerializedName("data") val data: List<Comment>

) {
    data class Comment(
        @field:SerializedName("content") val content: String,

        @field:SerializedName("author") val author: String
    )
}
