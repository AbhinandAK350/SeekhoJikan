package com.abhinand.seekhojikan.home.data.repository

import com.abhinand.seekhojikan.core.network.NetworkResource
import com.abhinand.seekhojikan.home.data.local.dao.AnimeDao
import com.abhinand.seekhojikan.home.data.mappers.toDomain
import com.abhinand.seekhojikan.home.data.mappers.toEntity
import com.abhinand.seekhojikan.home.data.remote.api.HomeApiService
import com.abhinand.seekhojikan.home.domain.model.Anime
import com.abhinand.seekhojikan.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val apiService: HomeApiService,
    private val animeDao: AnimeDao
) : HomeRepository {
    override fun getAnimeList(): Flow<NetworkResource<List<Anime>>> = flow {
        emit(NetworkResource.Loading)

        try {
            val response = apiService.getTopAnime()
            val remoteAnimeList = response.data.map { it.toEntity() }

            animeDao.insertAnimeList(remoteAnimeList)

            val newDbData = animeDao.getAnimeList().map { it.toDomain() }
            emit(NetworkResource.Success(newDbData))
            
        } catch (e: Exception) {
            val localAnimeList = animeDao.getAnimeList().map { it.toDomain() }
            if (localAnimeList.isNotEmpty()) {
                emit(NetworkResource.Success(localAnimeList))
            } else {
                emit(NetworkResource.Error(e.message ?: "Unknown error occurred"))
            }
        }
    }
}
