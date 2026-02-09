package com.glpalma.simpleweather.domain.repository

import com.glpalma.simpleweather.domain.model.CityInfo

interface CityRepository {
    suspend fun searchCity(
        name: String
    ): Result<List<CityInfo>>
}