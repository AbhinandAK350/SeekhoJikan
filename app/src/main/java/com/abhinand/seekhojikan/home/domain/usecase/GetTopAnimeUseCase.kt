package com.abhinand.seekhojikan.home.domain.usecase

import com.abhinand.seekhojikan.core.network.NetworkResource
import com.abhinand.seekhojikan.home.domain.model.Anime
import com.abhinand.seekhojikan.home.domain.repository.HomeRepository
import javax.inject.Inject

class GetTopAnimeUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(): NetworkResource<List<Anime>> {
        return repository.getAnimeList()
    }
}