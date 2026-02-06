package com.glpalma.simpleweather.data.repository

import com.glpalma.simpleweather.data.mapper.toDomain
import com.glpalma.simpleweather.data.remote.api.GeocodingApi
import com.glpalma.simpleweather.domain.model.CityInfo
import com.glpalma.simpleweather.domain.repository.LocationRepository
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val api: GeocodingApi
): LocationRepository {
    override suspend fun searchCity(
        name: String
    ): Result<List<CityInfo>> {
        return runCatching { api.searchCity(name).toDomain() }
    }
}