package com.glpalma.simpleweather.domain.repository

import com.glpalma.simpleweather.domain.model.WeatherReport

interface WeatherRepository {

    suspend fun getWeather(
        latitude: Double,
        longitude: Double
    ): Result<WeatherReport>
}