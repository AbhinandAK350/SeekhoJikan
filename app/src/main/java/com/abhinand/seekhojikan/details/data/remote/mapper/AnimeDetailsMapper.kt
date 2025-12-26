package com.abhinand.seekhojikan.details.data.remote.mapper

import com.abhinand.seekhojikan.details.data.remote.dto.AnimeDetailsDto
import com.abhinand.seekhojikan.details.domain.model.AnimeDetails

fun AnimeDetailsDto.toDomain(): AnimeDetails {
    return AnimeDetails(
        id = data.mal_id,
        title = data.title,
        imageUrl = data.images.jpg.large_image_url ?: data.images.jpg.image_url ?: "",
        trailerUrl = data.trailer?.url,
        synopsis = data.synopsis ?: "No synopsis available.",
        genres = data.genres.map { it.name },
        episodes = data.episodes ?: 0,
        rating = data.rating ?: "N/A",
        score = data.score?.toFloat() ?: 0.0f
    )
}