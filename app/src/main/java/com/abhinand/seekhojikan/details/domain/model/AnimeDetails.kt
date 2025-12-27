package com.abhinand.seekhojikan.details.domain.model

import com.abhinand.seekhojikan.home.data.remote.dto.NamedResourceDto

data class AnimeDetails(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val embeddedUrl: String?,
    val synopsis: String,
    val genres: List<NamedResourceDto>,
    val episodes: Int,
    val rating: String,
    val score: Float
)