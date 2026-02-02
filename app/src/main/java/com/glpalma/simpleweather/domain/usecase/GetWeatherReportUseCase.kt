package com.glpalma.simpleweather.domain.usecase

import com.glpalma.simpleweather.domain.model.WeatherReport
import com.glpalma.simpleweather.domain.repository.WeatherRepository

class GetWeatherReportUseCase(private val repository: WeatherRepository) {

    suspend operator fun invoke(
        lat: Double,
        lon: Double
    ): Result<WeatherReport> {
        return repository.getWeather(lat, lon)
    }
}