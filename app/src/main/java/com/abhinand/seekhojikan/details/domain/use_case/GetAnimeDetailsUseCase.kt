package com.abhinand.seekhojikan.details.domain.use_case

import com.abhinand.seekhojikan.core.network.NetworkResource
import com.abhinand.seekhojikan.details.domain.model.AnimeDetails
import com.abhinand.seekhojikan.details.domain.repository.DetailsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAnimeDetailsUseCase @Inject constructor(
    private val repository: DetailsRepository
) {
    operator fun invoke(id: Int): Flow<NetworkResource<AnimeDetails>> {
        return repository.getAnimeDetails(id)
    }
}