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
    override fun getAnimeList(
        forceRefresh: Boolean,
        page: Int
    ): Flow<NetworkResource<List<Anime>>> = flow {
        emit(NetworkResource.Loading)

        val pageSize = 25 // Default page size for Jikan API

        try {
            // Online path
            val response = apiService.getTopAnime(page = page)
            val remoteAnimeList = response.data.map { it.toEntity() }

            if (forceRefresh) {
                animeDao.clearAll()
            }
            animeDao.insertAnimeList(remoteAnimeList)

            // For online, just emit the data we just fetched and inserted
            emit(NetworkResource.Success(remoteAnimeList.map { it.toDomain() }))

        } catch (e: Exception) {
            // Offline path
            val offset = (page - 1) * pageSize
            val localAnimeList = animeDao.getAnimeList(limit = pageSize, offset = offset)
                .map { it.toDomain() }

            if (localAnimeList.isNotEmpty()) {
                emit(NetworkResource.Success(localAnimeList))
            } else {
                if (page == 1) {
                    emit(NetworkResource.Error(e.message ?: "Unknown error occurred"))
                } else {
                    // Reached end of pagination in offline mode
                    emit(NetworkResource.Success(emptyList()))
                }
            }
        }
    }
}