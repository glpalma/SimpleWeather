package com.glpalma.simpleweather.data.di

import android.app.Application
import androidx.room.Room
import com.glpalma.simpleweather.data.local.AppDatabase
import com.glpalma.simpleweather.data.local.dao.CityDao
import com.glpalma.simpleweather.data.local.dao.WeatherDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        app: Application
    ): AppDatabase =
        Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "simple_weather.db"
        ).build()

    @Provides
    fun provideWeatherDao(
        db: AppDatabase
    ): WeatherDao = db.weatherDao()

    @Provides
    fun provideCityDao(
        db: AppDatabase
    ): CityDao = db.cityDao()

}