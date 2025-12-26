package com.abhinand.seekhojikan.details.data.mapper

import com.abhinand.seekhojikan.details.domain.model.AnimeDetails
import com.abhinand.seekhojikan.home.data.local.entity.AnimeEntity

fun AnimeEntity.toDetailsDomain(): AnimeDetails {
    return AnimeDetails(
        id = malId,
        title = title,
        imageUrl = imageUrl,
        youtubeId = youtubeId,
        synopsis = synopsis ?: "N/A",
        genres = genres,
        episodes = episodes ?: 0,
        rating = rating,
        score = score?.toFloat() ?: 0.0f
    )
}
