package com.abhinand.seekhojikan.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhinand.seekhojikan.core.network.NetworkResource
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
    private val getTopAnimeUseCase: GetTopAnimeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState = _uiState.asStateFlow()

    init {
        getTopAnimeList()
    }

    private fun getTopAnimeList() {
        getTopAnimeUseCase().onEach { result ->
            when (result) {
                is NetworkResource.Success -> {
                    _uiState.update {
                        it.copy(
                            animeList = result.data ?: emptyList(),
                            isLoading = false,
                            isRefreshing = false
                        )
                    }
                }
                is NetworkResource.Error -> {
                    _uiState.update {
                        it.copy(
                            error = result.message,
                            isLoading = false,
                            isRefreshing = false
                        )
                    }
                }

                is NetworkResource.Loading -> {
                    _uiState.update {
                        if (it.animeList.isEmpty()) {
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