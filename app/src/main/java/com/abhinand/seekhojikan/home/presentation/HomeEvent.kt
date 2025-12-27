package com.abhinand.seekhojikan.home.presentation

sealed interface HomeEvent {
    data object OnLoadNextPage : HomeEvent
    data object OnRefresh : HomeEvent
}
