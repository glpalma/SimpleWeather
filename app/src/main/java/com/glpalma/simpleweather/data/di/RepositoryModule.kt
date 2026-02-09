package com.glpalma.simpleweather.data.di

import com.glpalma.simpleweather.data.remote.api.GeocodingApi
import com.glpalma.simpleweather.data.remote.api.OpenMeteoApi
import com.glpalma.simpleweather.data.repository.CityRepositoryImpl
import com.glpalma.simpleweather.data.repository.WeatherRepositoryImpl
import com.glpalma.simpleweather.domain.repository.CityRepository
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

    @Provides
    @Singleton
    fun provideGeocodingRepository(
        api: GeocodingApi
    ): CityRepository {
        return CityRepositoryImpl(api)
    }
}