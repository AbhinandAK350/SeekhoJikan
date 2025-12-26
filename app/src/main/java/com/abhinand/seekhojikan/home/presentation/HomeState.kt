package com.abhinand.seekhojikan.home.presentation

import com.abhinand.seekhojikan.home.domain.model.Anime

data class HomeState(
    val isLoading: Boolean = false,
    val animeList: List<Anime> = emptyList(),
    val error: String? = null,
    val isRefreshing: Boolean = false,
    val page: Int = 1,
    val isLoadingNextPage: Boolean = false,
    val isNetworkError: Boolean = false
)