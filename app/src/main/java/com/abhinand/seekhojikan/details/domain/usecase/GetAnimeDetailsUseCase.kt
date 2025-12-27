package com.abhinand.seekhojikan.details.domain.usecase

import com.abhinand.seekhojikan.core.network.NetworkResource
import com.abhinand.seekhojikan.details.domain.model.AnimeDetails
import com.abhinand.seekhojikan.details.domain.repository.DetailsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for getting the details of a specific anime.
 */
class GetAnimeDetailsUseCase @Inject constructor(
    private val repository: DetailsRepository
) {
    /**
     * Executes the use case.
     * @param id The ID of the anime to fetch.
     * @return A [Flow] of [NetworkResource] containing the [AnimeDetails].
     */
    operator fun invoke(id: Int): Flow<NetworkResource<AnimeDetails>> {
        return repository.getAnimeDetails(id)
    }
}