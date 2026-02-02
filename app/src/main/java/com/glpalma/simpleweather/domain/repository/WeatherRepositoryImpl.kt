package com.glpalma.simpleweather.domain.repository

import com.glpalma.simpleweather.domain.model.WeatherReport

class WeatherRepositoryImpl : WeatherRepository {
    override suspend fun getWeather(
        latitude: Double,
        longitude: Double
    ): Result<WeatherReport> {
        return Result.failure(NotImplementedError())
    }
}