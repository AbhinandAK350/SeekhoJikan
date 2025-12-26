package com.abhinand.seekhojikan.core.di

import android.content.Context
import com.abhinand.seekhojikan.core.network.RetrofitClient
import com.abhinand.seekhojikan.core.utils.ConnectivityObserver
import com.abhinand.seekhojikan.core.utils.NetworkConnectivityObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideRetrofit(): Retrofit = RetrofitClient.create()

    @Provides
    @Singleton
    fun provideConnectivityObserver(@ApplicationContext context: Context): ConnectivityObserver {
        return NetworkConnectivityObserver(context)
    }

}