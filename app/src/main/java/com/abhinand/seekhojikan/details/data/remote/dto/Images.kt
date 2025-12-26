package com.abhinand.seekhojikan.details.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class Images(
    val jpg: Jpg,
    val webp: Webp
)

@Serializable
data class Jpg(
    val image_url: String?,
    val small_image_url: String?,
    val large_image_url: String?
)

@Serializable
data class Webp(
    val image_url: String?,
    val small_image_url: String?,
    val large_image_url: String?
)