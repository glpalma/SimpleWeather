package com.glpalma.simpleweather.data.di

import com.glpalma.simpleweather.data.remote.api.OpenMeteoApi
import com.glpalma.simpleweather.data.repository.WeatherRepositoryImpl
import com.glpalma.simpleweather.domain.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideWeatherRepository(
        api: OpenMeteoApi
    ): WeatherRepository {
        return WeatherRepositoryImpl(api)
    }
}