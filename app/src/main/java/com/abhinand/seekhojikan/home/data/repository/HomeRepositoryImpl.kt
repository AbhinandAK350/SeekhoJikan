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

    private val pageSize = 25

    override fun getAnimeList(page: Int): Flow<NetworkResource<List<Anime>>> = flow {
        emit(NetworkResource.Loading)

        try {
            val response = apiService.getTopAnime(page)
            val entities = response.data.map { it.toEntity() }

            if (page == 1) {
                animeDao.clearAll()
            }

            animeDao.insertAnimeList(entities)

            emit(NetworkResource.Success(entities.map { it.toDomain() }))
        } catch (e: Exception) {
            val offset = (page - 1) * pageSize
            val cached = animeDao.getAnimeList(pageSize, offset)
                .map { it.toDomain() }

            if (cached.isNotEmpty()) {
                emit(NetworkResource.Success(cached))
            } else {
                emit(NetworkResource.Error(e.message ?: "Network error"))
            }
        }
    }
}