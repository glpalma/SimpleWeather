package com.glpalma.simpleweather.domain.repository

import com.glpalma.simpleweather.domain.model.CityInfo
import com.glpalma.simpleweather.domain.model.WeatherReport

interface WeatherRepository {

    suspend fun getWeather(
        cityInfo: CityInfo
    ): Result<WeatherReport>

    suspend fun getCachedWeatherForCity(cityId: String): Result<WeatherReport?>

}