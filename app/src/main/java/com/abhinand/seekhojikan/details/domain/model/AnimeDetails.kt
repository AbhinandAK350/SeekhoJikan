package com.abhinand.seekhojikan.details.domain.model

data class AnimeDetails(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val trailerUrl: String?,
    val synopsis: String,
    val genres: List<String>,
    val episodes: Int,
    val rating: String,
    val score: Float
)