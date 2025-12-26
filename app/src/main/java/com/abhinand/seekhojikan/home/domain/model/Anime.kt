package com.abhinand.seekhojikan.home.domain.model

data class Anime(
    val malId: Int,
    val title: String,
    val imageUrl: String,
    val score: Double?,
    val year: Int?,
    val synopsis: String?,
    val rank: Int?,
    val episodes: Int?
)
