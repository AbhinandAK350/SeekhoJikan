package com.abhinand.seekhojikan.details.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class Trailer(
    val youtube_id: String?,
    val url: String?,
    val embed_url: String?
)