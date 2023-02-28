package com.example.apiapplication

import kotlinx.serialization.*

@Serializable
data class Hero(
    val attack_type: String,
    val id: Int,
    val legs: Int,
    val localized_name: String,
    val name: String,
    val primary_attr: String,
    val roles: List<String>
)