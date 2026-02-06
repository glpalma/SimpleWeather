package com.glpalma.simpleweather.domain.usecase

import com.glpalma.simpleweather.domain.model.CityInfo
import com.glpalma.simpleweather.domain.repository.LocationRepository
import javax.inject.Inject

class GetCityListFromNameUseCase @Inject constructor(
    private val repository: LocationRepository
) {

    suspend operator fun invoke(
        cityName: String
    ): Result<List<CityInfo>> {
        return repository.searchCity(cityName)
    }
}