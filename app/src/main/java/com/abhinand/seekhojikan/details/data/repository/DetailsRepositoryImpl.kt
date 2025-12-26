package com.abhinand.seekhojikan.details.data.repository

import com.abhinand.seekhojikan.core.network.NetworkResource
import com.abhinand.seekhojikan.details.data.mapper.toDetailsDomain
import com.abhinand.seekhojikan.details.data.mapper.toDomain
import com.abhinand.seekhojikan.details.data.mapper.toEntity
import com.abhinand.seekhojikan.details.data.remote.api.DetailsApiService
import com.abhinand.seekhojikan.details.domain.model.AnimeDetails
import com.abhinand.seekhojikan.details.domain.repository.DetailsRepository
import com.abhinand.seekhojikan.home.data.local.dao.AnimeDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class DetailsRepositoryImpl @Inject constructor(
    private val apiService: DetailsApiService,
    private val animeDao: AnimeDao
) : DetailsRepository {

    override fun getAnimeDetails(id: Int): Flow<NetworkResource<AnimeDetails>> = flow {
        emit(NetworkResource.Loading)

        try {
            val animeDetails = apiService.getAnimeDetails(id).toDomain()
            animeDao.updateAnime(animeDetails.toEntity())

            emit(NetworkResource.Success(animeDetails))
        } catch (e: IOException) {
            val animeFromDb = animeDao.getAnime(id)
            if (animeFromDb != null) {
                emit(NetworkResource.Success(animeFromDb.toDetailsDomain()))
            } else {
                emit(NetworkResource.Error("Couldn't reach server and no cached data available."))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(NetworkResource.Error("An unexpected error occurred: ${e.message}"))
        }
    }
}