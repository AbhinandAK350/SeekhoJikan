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

/**
 * This repository manages anime data for the home screen.
 * It uses a network-over-local-cache strategy, fetching data from the Jikan API
 * and storing it in a local Room database to support offline viewing and reduce network usage.
 */
class HomeRepositoryImpl @Inject constructor(
    private val apiService: HomeApiService,
    private val animeDao: AnimeDao
) : HomeRepository {

    private val pageSize = 25

    /**
     * Fetches a paginated list of top anime.
     *
     * This function first attempts to fetch data from the remote API. If successful, it clears
     * the local database on the first page load and inserts the new data. If the network request
     * fails, it falls back to the local database to provide offline support.
     *
     * @param page The page number to retrieve.
     * @return A Flow that emits the current loading status and the list of anime.
     */
    override fun getAnimeList(page: Int): Flow<NetworkResource<List<Anime>>> = flow {
        emit(NetworkResource.Loading)

        try {
            // Fetch from the network and map to database entities
            val response = apiService.getTopAnime(page)
            val entities = response.data.map { it.toEntity() }

            // Clear the cache when loading the first page
            if (page == 1) {
                animeDao.clearAll()
            }

            // Save the new data to the database
            animeDao.insertAnimeList(entities)

            // Emit the newly fetched data
            emit(NetworkResource.Success(entities.map { it.toDomain() }))
        } catch (e: Exception) {
            // On network error, fall back to the local cache
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