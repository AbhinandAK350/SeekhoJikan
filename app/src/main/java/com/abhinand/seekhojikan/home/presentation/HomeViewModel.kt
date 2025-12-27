package com.abhinand.seekhojikan.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhinand.seekhojikan.core.network.NetworkResource
import com.abhinand.seekhojikan.core.utils.ConnectivityObserver
import com.abhinand.seekhojikan.home.data.local.dao.AnimeDao
import com.abhinand.seekhojikan.home.domain.usecase.GetTopAnimeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTopAnimeUseCase: GetTopAnimeUseCase,
    private val connectivityObserver: ConnectivityObserver,
    private val animeDao: AnimeDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        observeConnectivity()
        loadNextPage()
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.OnLoadNextPage -> loadNextPage()
            HomeEvent.OnRefresh -> refreshFromNetwork()
        }
    }

    private fun observeConnectivity() {
        connectivityObserver.observe()
            .distinctUntilChanged()
            .onEach { status ->
                if (status == ConnectivityObserver.Status.Available) {
                    refreshFromNetwork()
                }
            }
            .launchIn(viewModelScope)
    }

    private fun refreshFromNetwork() {
        loadJob?.cancel()

        viewModelScope.launch {
            animeDao.clearAll()

            _uiState.value = HomeState()

            loadNextPage()
        }
    }

    private fun loadNextPage() {
        val state = _uiState.value
        if (state.isLoading || state.endReached) return

        loadJob?.cancel()
        loadJob = getTopAnimeUseCase(state.page)
            .onEach { result ->
                when (result) {
                    is NetworkResource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }

                    is NetworkResource.Success -> {
                        val data = result.data.orEmpty()
                        _uiState.update {
                            it.copy(
                                animeList = it.animeList + data,
                                page = it.page + 1,
                                isLoading = false,
                                endReached = data.isEmpty(),
                                isNetworkError = false,
                                error = null
                            )
                        }
                    }

                    is NetworkResource.Error -> {
                        val isNetworkError =
                            result.message?.contains("Unable to resolve host", true) == true

                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isNetworkError = isNetworkError,
                                error = if (isNetworkError) null else result.message
                            )
                        }
                    }
                }
            }
            .launchIn(viewModelScope)
    }
}