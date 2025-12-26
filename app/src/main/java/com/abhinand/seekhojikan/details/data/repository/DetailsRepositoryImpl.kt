package com.abhinand.seekhojikan.details.data.repository

import com.abhinand.seekhojikan.core.network.NetworkResource
import com.abhinand.seekhojikan.details.data.remote.api.DetailsApiService
import com.abhinand.seekhojikan.details.data.remote.mapper.toDomain
import com.abhinand.seekhojikan.details.domain.model.AnimeDetails
import com.abhinand.seekhojikan.details.domain.repository.DetailsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class DetailsRepositoryImpl @Inject constructor(
    private val apiService: DetailsApiService
) : DetailsRepository {

    override fun getAnimeDetails(id: Int): Flow<NetworkResource<AnimeDetails>> = flow {
        emit(NetworkResource.Loading)
        try {
            val animeDetails = apiService.getAnimeDetails(id).toDomain()
            emit(NetworkResource.Success(animeDetails))
        } catch (e: IOException) {
            emit(NetworkResource.Error("Couldn't reach server. Check your internet connection."))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(NetworkResource.Error("An unexpected error occurred: ${e.message}"))
        }
    }
}