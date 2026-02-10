package com.glpalma.simpleweather.domain.usecase

import com.glpalma.simpleweather.domain.model.CityInfo
import com.glpalma.simpleweather.domain.model.WeatherReport
import com.glpalma.simpleweather.domain.repository.WeatherRepository
import javax.inject.Inject

class GetWeatherReportUseCase @Inject constructor(
    private val repository: WeatherRepository
) {

    suspend operator fun invoke(
        cityInfo: CityInfo
    ): Result<WeatherReport> {
        return repository.getWeather(cityInfo)
    }
}