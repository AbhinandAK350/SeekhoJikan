package com.abhinand.seekhojikan.details.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhinand.seekhojikan.core.network.NetworkResource
import com.abhinand.seekhojikan.core.utils.ConnectivityObserver
import com.abhinand.seekhojikan.details.domain.usecase.GetAnimeDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getAnimeDetailsUseCase: GetAnimeDetailsUseCase,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    private val _state = mutableStateOf(DetailsState())
    val state: State<DetailsState> = _state

    private var animeId: Int? = null

    init {
        connectivityObserver.observe().onEach { status ->
            val isOnline = status == ConnectivityObserver.Status.Available
            _state.value = _state.value.copy(isOnline = isOnline)
            if (isOnline) {
                animeId?.let { id -> getAnimeDetails(id) }
            }
        }.launchIn(viewModelScope)
    }

    fun getAnimeDetails(id: Int) {
        this.animeId = id
        getAnimeDetailsUseCase(id).onEach {
            when (it) {
                is NetworkResource.Loading -> {
                    _state.value = _state.value.copy(isLoading = true)
                }

                is NetworkResource.Success -> {
                    _state.value =
                        _state.value.copy(isLoading = false, animeDetails = it.data, error = null)
                }

                is NetworkResource.Error -> {
                    _state.value =
                        _state.value.copy(
                            isLoading = false,
                            error = it.message ?: "An unexpected error occurred"
                        )
                }
            }
        }.launchIn(viewModelScope)
    }
}