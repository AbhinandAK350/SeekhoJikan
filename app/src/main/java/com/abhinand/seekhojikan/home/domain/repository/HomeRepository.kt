package com.abhinand.seekhojikan.home.domain.repository

import com.abhinand.seekhojikan.core.network.NetworkResource
import com.abhinand.seekhojikan.home.domain.model.Anime
import kotlinx.coroutines.flow.Flow

interface HomeRepository {

    suspend fun getAnimeList(): NetworkResource<List<Anime>>

}
