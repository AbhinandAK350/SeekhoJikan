package com.abhinand.seekhojikan.home.data.mappers

import com.abhinand.seekhojikan.home.data.local.entity.AnimeEntity
import com.abhinand.seekhojikan.home.data.remote.dto.AnimeDto

fun AnimeDto.toEntity(): AnimeEntity {
    return AnimeEntity(
        malId = mal_id,
        title = title,
        imageUrl = images.jpg.image_url,
        score = score,
        year = year,
        synopsis = synopsis,
        rank = rank,
        episodes = episodes
    )
}
