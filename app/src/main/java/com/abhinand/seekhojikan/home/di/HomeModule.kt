package com.abhinand.seekhojikan.home.di

import com.abhinand.seekhojikan.home.data.remote.api.HomeApiService
import com.abhinand.seekhojikan.home.data.repository.HomeRepositoryImpl
import com.abhinand.seekhojikan.home.domain.repository.HomeRepository
import com.abhinand.seekhojikan.home.domain.usecase.GetTopAnimeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomeModule {

    @Provides
    @Singleton
    fun provideHomeApiService(retrofit: Retrofit): HomeApiService {
        return retrofit.create(HomeApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideHomeRepository(apiService: HomeApiService): HomeRepository {
        return HomeRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideGetTopAnimeUseCase(repository: HomeRepository): GetTopAnimeUseCase {
        return GetTopAnimeUseCase(repository)
    }
}