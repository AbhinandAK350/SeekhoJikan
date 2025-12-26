package com.abhinand.seekhojikan.home.data.remote.api

import com.abhinand.seekhojikan.home.data.remote.dto.TopAnimeResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface HomeApiService {

    @GET("top/anime")
    suspend fun getTopAnime(
        @Query("page") page: Int
    ): TopAnimeResponseDto

}
