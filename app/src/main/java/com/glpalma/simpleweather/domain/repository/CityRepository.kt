package com.glpalma.simpleweather.domain.repository

import com.glpalma.simpleweather.domain.model.CityInfo

interface CityRepository {
    suspend fun searchCityByName(
        name: String
    ): Result<List<CityInfo>>

    suspend fun saveCity(
        cityInfo: CityInfo
    ): Result<Unit>

    suspend fun deleteCity(
        cityInfo: CityInfo
    ): Result<Unit>

    suspend fun getCities(): Result<List<CityInfo>>
}
