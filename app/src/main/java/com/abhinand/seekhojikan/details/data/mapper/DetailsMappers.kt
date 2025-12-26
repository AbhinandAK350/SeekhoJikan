package com.abhinand.seekhojikan.details.data.mapper

import com.abhinand.seekhojikan.details.data.remote.dto.AnimeDetailsDto
import com.abhinand.seekhojikan.details.data.remote.dto.Genre
import com.abhinand.seekhojikan.details.domain.model.AnimeDetails
import com.abhinand.seekhojikan.home.data.remote.dto.NamedResourceDto

fun AnimeDetailsDto.toDomain(): AnimeDetails {
    val anime = data
    return AnimeDetails(
        id = anime.mal_id,
        title = anime.title,
        imageUrl = anime.images.jpg.large_image_url ?: anime.images.jpg.image_url ?: "",
        youtubeId = anime.trailer?.youtube_id,
        synopsis = anime.synopsis ?: "No synopsis available.",
        genres = anime.genres.map { it.toDomain() },
        episodes = anime.episodes ?: 0,
        rating = anime.rating ?: "N/A",
        score = anime.score ?: 0f
    )
}

fun Genre.toDomain(): NamedResourceDto {
    return NamedResourceDto(
        mal_id = mal_id,
        type = type,
        name = name,
        url = url
    )
}
