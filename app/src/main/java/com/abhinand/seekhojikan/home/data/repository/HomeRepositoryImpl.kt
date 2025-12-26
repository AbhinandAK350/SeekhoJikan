package com.abhinand.seekhojikan.home.data.repository

import com.abhinand.seekhojikan.core.network.NetworkResource
import com.abhinand.seekhojikan.home.data.mappers.toDomain
import com.abhinand.seekhojikan.home.data.remote.api.HomeApiService
import com.abhinand.seekhojikan.home.domain.model.Anime
import com.abhinand.seekhojikan.home.domain.repository.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val apiService: HomeApiService
) : HomeRepository {
    override suspend fun getAnimeList(): NetworkResource<List<Anime>> {
        return try {
            val response = apiService.getTopAnime()
            val animeList = response.data.map { it.toDomain() }
            NetworkResource.Success(animeList)
        } catch (e: Exception) {
            NetworkResource.Error(e.message)
        }
    }
}
