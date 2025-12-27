package com.abhinand.seekhojikan.details.di

import com.abhinand.seekhojikan.core.network.RetrofitClient
import com.abhinand.seekhojikan.details.data.remote.api.DetailsApiService
import com.abhinand.seekhojikan.details.data.repository.DetailsRepositoryImpl
import com.abhinand.seekhojikan.details.domain.repository.DetailsRepository
import com.abhinand.seekhojikan.details.domain.usecase.GetAnimeDetailsUseCase
import com.abhinand.seekhojikan.home.data.local.dao.AnimeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DetailsModule {

    @Provides
    @Singleton
    fun provideDetailsApiService(): DetailsApiService {
        return RetrofitClient.create().create(DetailsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDetailsRepository(
        apiService: DetailsApiService,
        animeDao: AnimeDao
    ): DetailsRepository {
        return DetailsRepositoryImpl(apiService, animeDao)
    }

    @Provides
    @Singleton
    fun provideGetAnimeDetailsUseCase(repository: DetailsRepository): GetAnimeDetailsUseCase {
        return GetAnimeDetailsUseCase(repository)
    }
}