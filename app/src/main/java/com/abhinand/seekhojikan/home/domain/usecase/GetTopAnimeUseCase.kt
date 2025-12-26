package com.abhinand.seekhojikan.home.domain.usecase

import com.abhinand.seekhojikan.core.network.NetworkResource
import com.abhinand.seekhojikan.home.domain.model.Anime
import com.abhinand.seekhojikan.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTopAnimeUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    operator fun invoke(): Flow<NetworkResource<List<Anime>>> {
        return repository.getAnimeList()
    }
}