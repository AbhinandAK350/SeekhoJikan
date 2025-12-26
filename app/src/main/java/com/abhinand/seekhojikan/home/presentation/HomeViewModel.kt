package com.abhinand.seekhojikan.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhinand.seekhojikan.core.network.NetworkResource
import com.abhinand.seekhojikan.core.utils.ConnectivityObserver
import com.abhinand.seekhojikan.home.domain.usecase.GetTopAnimeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTopAnimeUseCase: GetTopAnimeUseCase,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState = _uiState.asStateFlow()

    init {
        onEvent(HomeEvent.OnRefresh)

        connectivityObserver.observe().onEach { 
            if (it == ConnectivityObserver.Status.Available) {
                onEvent(HomeEvent.OnRefresh)
            }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnRefresh -> {
                getTopAnimeList(forceRefresh = true, page = 1)
            }
            is HomeEvent.OnLoadNextPage -> {
                if (uiState.value.isLoadingNextPage) return
                _uiState.update {
                    it.copy(page = it.page + 1)
                }
                getTopAnimeList(page = _uiState.value.page)
            }
        }
    }

    private fun getTopAnimeList(forceRefresh: Boolean = false, page: Int) {
        getTopAnimeUseCase(forceRefresh, page).onEach { result ->
            when (result) {
                is NetworkResource.Success -> {
                    _uiState.update {
                        val newList = result.data ?: emptyList()
                        if (forceRefresh || page == 1) {
                            it.copy(
                                animeList = newList,
                                isLoading = false,
                                isRefreshing = false,
                                isLoadingNextPage = false
                            )
                        } else {
                            it.copy(
                                animeList = it.animeList + newList,
                                isLoading = false,
                                isRefreshing = false,
                                isLoadingNextPage = false
                            )
                        }
                    }
                }
                is NetworkResource.Error -> {
                    _uiState.update {
                        it.copy(
                            error = result.message,
                            isLoading = false,
                            isRefreshing = false,
                            isLoadingNextPage = false
                        )
                    }
                }

                is NetworkResource.Loading -> {
                    _uiState.update {
                        if (forceRefresh) {
                            it.copy(animeList = emptyList(), isLoading = true)
                        } else if (page > 1) {
                            it.copy(isLoadingNextPage = true)
                        } else if (it.animeList.isEmpty()) {
                            it.copy(isLoading = true)
                        } else {
                            it.copy(isRefreshing = true)
                        }
                    }
                }
            }
        }.launchIn(viewModelScope)
    }
}