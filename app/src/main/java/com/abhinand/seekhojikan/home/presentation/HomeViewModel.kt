package com.abhinand.seekhojikan.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhinand.seekhojikan.core.network.NetworkResource
import com.abhinand.seekhojikan.home.domain.usecase.GetTopAnimeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val response = getTopAnimeUseCase()) {
                is NetworkResource.Success -> {
                    _uiState.update {
                        it.copy(
                            animeList = response.data ?: emptyList(),
                            isLoading = false
                        )
                    }
                }
                is NetworkResource.Error -> {
                    _uiState.update { it.copy(error = response.message, isLoading = false) }
                }
                is NetworkResource.Loading -> Unit
            }
        }
    }
}