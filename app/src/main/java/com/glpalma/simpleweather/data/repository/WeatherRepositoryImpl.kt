package com.glpalma.simpleweather.data.repository

import com.glpalma.simpleweather.data.local.dao.WeatherDao
import com.glpalma.simpleweather.data.mapper.toDomain
import com.glpalma.simpleweather.data.mapper.toEntity
import com.glpalma.simpleweather.data.remote.api.OpenMeteoApi
import com.glpalma.simpleweather.domain.model.CityInfo
import com.glpalma.simpleweather.domain.model.WeatherReport
import com.glpalma.simpleweather.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: OpenMeteoApi,
    private val weatherDao: WeatherDao
) : WeatherRepository {
    override suspend fun getWeather(
        cityInfo: CityInfo
    ): Result<WeatherReport> {
        return runCatching {
            val dto = api.getForecast(cityInfo.latitude, cityInfo.longitude)
            try {
                weatherDao.upsert(dto.toEntity(cityInfo.id))
            } catch (_: Exception) {
                // Cache write is best-effort; FK violation expected for transient cities
            }
            dto.toDomain()
        }
    }

    override suspend fun getCachedWeatherForCity(cityId: String): Result<WeatherReport?> {
        return runCatching { weatherDao.getWeather(cityId)?.toDomain() }
    }
}