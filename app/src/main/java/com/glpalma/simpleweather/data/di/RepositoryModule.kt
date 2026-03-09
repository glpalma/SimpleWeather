package com.glpalma.simpleweather.data.di

import android.content.Context
import com.glpalma.simpleweather.data.local.dao.CityDao
import com.glpalma.simpleweather.data.local.dao.WeatherDao
import com.glpalma.simpleweather.data.remote.api.GeocodingApi
import com.glpalma.simpleweather.data.remote.api.OpenMeteoApi
import com.glpalma.simpleweather.data.repository.CityRepositoryImpl
import com.glpalma.simpleweather.data.repository.LocationRepositoryImpl
import com.glpalma.simpleweather.data.repository.WeatherRepositoryImpl
import com.glpalma.simpleweather.domain.repository.CityRepository
import com.glpalma.simpleweather.domain.repository.LocationRepository
import com.glpalma.simpleweather.domain.repository.WeatherRepository
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideWeatherRepository(
        api: OpenMeteoApi,
        weatherDao: WeatherDao
    ): WeatherRepository {
        return WeatherRepositoryImpl(api, weatherDao)
    }

    @Provides
    @Singleton
    fun provideGeocodingRepository(
        api: GeocodingApi,
        cityDao: CityDao
    ): CityRepository {
        return CityRepositoryImpl(api, cityDao)
    }

    @Provides
    @Singleton
    fun provideLocationRepository(
        locationClient: FusedLocationProviderClient,
        @ApplicationContext context: Context
    ): LocationRepository {
        return LocationRepositoryImpl(locationClient, context)
    }
}