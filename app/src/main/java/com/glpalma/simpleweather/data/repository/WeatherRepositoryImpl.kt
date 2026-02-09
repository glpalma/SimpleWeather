package com.glpalma.simpleweather.data.repository

import com.glpalma.simpleweather.data.mapper.toDomain
import com.glpalma.simpleweather.data.remote.api.OpenMeteoApi
import com.glpalma.simpleweather.domain.model.WeatherReport
import com.glpalma.simpleweather.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: OpenMeteoApi
) : WeatherRepository {
    override suspend fun getWeather(
        latitude: Double,
        longitude: Double
    ): Result<WeatherReport> {
        return runCatching { api.getForecast(latitude, longitude).toDomain() }
        // todo: add database
    }
}