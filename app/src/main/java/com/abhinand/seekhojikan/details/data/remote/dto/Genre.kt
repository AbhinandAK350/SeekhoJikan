package com.abhinand.seekhojikan.details.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class Genre(
    val mal_id: Int,
    val type: String,
    val name: String,
    val url: String
)