package com.abhinand.seekhojikan.details.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhinand.seekhojikan.core.network.NetworkResource
import com.abhinand.seekhojikan.details.domain.use_case.GetAnimeDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getAnimeDetailsUseCase: GetAnimeDetailsUseCase
) : ViewModel() {

    private val _state = mutableStateOf(DetailsState())
    val state: State<DetailsState> = _state

    fun getAnimeDetails(id: Int) {
        getAnimeDetailsUseCase(id).onEach {
            when (it) {
                is NetworkResource.Loading -> {
                    _state.value = DetailsState(isLoading = true)
                }

                is NetworkResource.Success -> {
                    _state.value = DetailsState(animeDetails = it.data)
                }

                is NetworkResource.Error -> {
                    _state.value =
                        DetailsState(error = it.message ?: "An unexpected error occurred")
                }
            }
        }.launchIn(viewModelScope)
    }
}