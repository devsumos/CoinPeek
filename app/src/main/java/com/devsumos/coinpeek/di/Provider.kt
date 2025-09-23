package com.devsumos.coinpeek.di

import com.devsumos.coinpeek.data.remote.CoinDeskApi
import com.devsumos.coinpeek.data.repo.CoinRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Provider {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://data-api.coindesk.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideCoinDeskApi(retrofit: Retrofit): CoinDeskApi =
        retrofit.create(CoinDeskApi::class.java)


    @Provides
    @Singleton
    fun provideCoinRepository(
        api: CoinDeskApi
    ): CoinRepository = CoinRepository(api)
}