package com.abhinand.seekhojikan.details.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val mal_id: Int,
    val url: String,
    val images: Images,
    val trailer: Trailer?,
    val title: String,
    val title_english: String?,
    val title_japanese: String?,
    val episodes: Int?,
    val status: String,
    val rating: String?,
    val score: Float?,
    val synopsis: String?,
    val year: Int?,
    val genres: List<Genre>,
    val studios: List<Studio>
)