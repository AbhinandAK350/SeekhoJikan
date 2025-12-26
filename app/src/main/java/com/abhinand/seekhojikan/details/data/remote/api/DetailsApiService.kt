package com.abhinand.seekhojikan.details.data.remote.api

import com.abhinand.seekhojikan.details.data.remote.dto.AnimeDetailsDto
import retrofit2.http.GET
import retrofit2.http.Path

interface DetailsApiService {
    @GET("anime/{id}")
    suspend fun getAnimeDetails(
        @Path("id") id: Int
    ): AnimeDetailsDto
}