package com.abhinand.seekhojikan.home.data.mappers

import com.abhinand.seekhojikan.home.data.remote.dto.AnimeDto
import com.abhinand.seekhojikan.home.domain.model.Anime

fun AnimeDto.toDomain(): Anime {
    return Anime(
        malId = mal_id,
        title = title,
        imageUrl = images.webp.large_image_url,
        score = score,
        year = year,
        synopsis = synopsis,
        rank = rank,
        episodes = episodes
    )
}
