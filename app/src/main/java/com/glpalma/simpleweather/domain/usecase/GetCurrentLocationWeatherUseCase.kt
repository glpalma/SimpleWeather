package com.glpalma.simpleweather.domain.usecase

import com.glpalma.simpleweather.domain.model.DisplayInfo
import com.glpalma.simpleweather.domain.repository.LocationRepository
import com.glpalma.simpleweather.domain.repository.WeatherRepository
import javax.inject.Inject

class GetCurrentLocationWeatherUseCase @Inject constructor(
    private val locationRepository: LocationRepository,
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(): Result<DisplayInfo> {
        return locationRepository.getCurrentLocation().mapCatching { cityInfo ->
            val report = weatherRepository.getWeather(cityInfo).getOrThrow()
            DisplayInfo(cityInfo = cityInfo, report = report)
        }
    }
}
