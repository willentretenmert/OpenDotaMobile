package com.example.apiapplication.data.models

import com.google.gson.annotations.SerializedName

data class Hero(
    @field:SerializedName("id")
    val id: Int,
    @field:SerializedName("localized_name")
    val localized_name: String,
    @field:SerializedName("name")
    val name: String,
)
