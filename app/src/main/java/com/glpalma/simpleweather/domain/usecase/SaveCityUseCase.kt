package com.glpalma.simpleweather.domain.usecase

import com.glpalma.simpleweather.domain.model.CityInfo
import com.glpalma.simpleweather.domain.repository.CityRepository
import javax.inject.Inject

class SaveCityUseCase @Inject constructor(
    private val repository: CityRepository
) {
    suspend operator fun invoke(cityInfo: CityInfo): Result<Unit> {
        return repository.saveCity(cityInfo)
    }
}
