package com.abhinand.seekhojikan.details.presentation

import com.abhinand.seekhojikan.details.domain.model.AnimeDetails

data class DetailsState(
    val animeDetails: AnimeDetails? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)