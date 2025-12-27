package com.abhinand.seekhojikan.details.domain.repository

import com.abhinand.seekhojikan.core.network.NetworkResource
import com.abhinand.seekhojikan.details.domain.model.AnimeDetails
import kotlinx.coroutines.flow.Flow

/**
 * Repository for fetching anime details.
 */
interface DetailsRepository {
    /**
     * Fetches the details for a specific anime.
     * @param id The ID of the anime to fetch.
     * @return A [Flow] of [NetworkResource] containing the [AnimeDetails].
     */
    fun getAnimeDetails(id: Int): Flow<NetworkResource<AnimeDetails>>
}