package com.abhinand.seekhojikan.home.data.remote.api

import com.abhinand.seekhojikan.home.data.remote.dto.TopAnimeResponseDto
import retrofit2.http.GET

interface HomeApiService {

    @GET("v4/top/anime")
    suspend fun getTopAnime(): TopAnimeResponseDto

}
