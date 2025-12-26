package com.abhinand.seekhojikan.details.domain.repository

import com.abhinand.seekhojikan.core.network.NetworkResource
import com.abhinand.seekhojikan.details.domain.model.AnimeDetails
import kotlinx.coroutines.flow.Flow

interface DetailsRepository {
    fun getAnimeDetails(id: Int): Flow<NetworkResource<AnimeDetails>>
}