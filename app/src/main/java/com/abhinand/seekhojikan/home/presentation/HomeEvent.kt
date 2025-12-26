package com.abhinand.seekhojikan.home.presentation

sealed class HomeEvent {
    data object OnRefresh : HomeEvent()
    data object OnLoadNextPage : HomeEvent()
}