package com.glpalma.simpleweather.domain.usecase

import com.glpalma.simpleweather.domain.model.DisplayInfo
import com.glpalma.simpleweather.domain.repository.CityRepository
import com.glpalma.simpleweather.domain.repository.WeatherRepository
import javax.inject.Inject

class GetSavedCitiesWithWeatherUseCase @Inject constructor(
    private val cityRepository: CityRepository,
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(): Result<List<DisplayInfo>> {
        return cityRepository.getCities().mapCatching { cities ->
            cities.mapNotNull { city ->
                weatherRepository.getCachedWeatherForCity(city.id)
                    .getOrNull()
                    ?.let { report -> DisplayInfo(cityInfo = city, report = report) }
            }
        }
    }
}
