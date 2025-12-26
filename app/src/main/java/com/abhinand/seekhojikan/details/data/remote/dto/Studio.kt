package com.abhinand.seekhojikan.details.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class Studio(
    val mal_id: Int,
    val type: String,
    val name: String,
    val url: String
)