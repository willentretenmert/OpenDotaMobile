package com.example.apiapplication.data.models

import com.google.gson.annotations.SerializedName

data class Hero(
    @field:SerializedName("attack_type")
    val attack_type: String,
    @field:SerializedName("id")
    val id: Int,
    @field:SerializedName("legs")
    val legs: Int,
    @field:SerializedName("localized_name")
    val localized_name: String,
    @field:SerializedName("name")
    val name: String,
    @field:SerializedName("primary_attr")
    val primary_attr: String,
    @field:SerializedName("roles")
    val roles: List<String>,
    @field:SerializedName("parents")
    val parents: List<String?>? = null
)
