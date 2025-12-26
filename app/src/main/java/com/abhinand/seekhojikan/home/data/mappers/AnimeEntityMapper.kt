package com.abhinand.seekhojikan.home.data.mappers

import com.abhinand.seekhojikan.home.data.local.entity.AnimeEntity
import com.abhinand.seekhojikan.home.domain.model.Anime

fun AnimeEntity.toDomain(): Anime {
    return Anime(
        malId = malId,
        title = title,
        imageUrl = imageUrl,
        score = score,
        year = year,
        synopsis = synopsis,
        rank = rank,
        episodes = episodes
    )
}
