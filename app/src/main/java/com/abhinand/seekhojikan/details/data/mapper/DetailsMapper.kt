package com.abhinand.seekhojikan.details.data.mapper

import com.abhinand.seekhojikan.details.data.remote.dto.AnimeDetailsDto
import com.abhinand.seekhojikan.details.domain.model.AnimeDetails
import com.abhinand.seekhojikan.home.data.local.entity.AnimeEntity

fun AnimeDetailsDto.toDomain(): AnimeDetails {
    return AnimeDetails(
        id = data.mal_id,
        title = data.title,
        imageUrl = data.images.jpg.large_image_url ?: data.images.jpg.image_url ?: "",
        embeddedUrl = data.trailer?.embed_url ?: "",
        synopsis = data.synopsis ?: "No Synopsis Available",
        genres = data.genres,
        episodes = data.episodes ?: 0,
        rating = data.rating ?: "N/A",
        score = data.score ?: 0.0f
    )
}

fun AnimeEntity.toDetailsDomain(): AnimeDetails {
    return AnimeDetails(
        id = malId,
        title = title,
        imageUrl = imageUrl,
        embeddedUrl = embeddedUrl,
        synopsis = synopsis ?: "No synopsis available.",
        genres = genres,
        episodes = episodes ?: 0,
        rating = rating,
        score = score?.toFloat() ?: 0.0f
    )
}

fun AnimeDetails.toEntity(): AnimeEntity {
    return AnimeEntity(
        malId = id,
        title = title,
        synopsis = synopsis,
        imageUrl = imageUrl,
        episodes = null,
        score = null,
        year = null,
        rank = null,
        embeddedUrl = embeddedUrl,
        genres = genres,
        rating = rating
    )
}