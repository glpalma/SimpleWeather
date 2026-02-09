package com.glpalma.simpleweather.data.repository

import com.glpalma.simpleweather.data.local.dao.CityDao
import com.glpalma.simpleweather.data.mapper.cityEntityListToCityInfoList
import com.glpalma.simpleweather.data.mapper.toDomain
import com.glpalma.simpleweather.data.mapper.toEntity
import com.glpalma.simpleweather.data.remote.api.GeocodingApi
import com.glpalma.simpleweather.domain.model.CityInfo
import com.glpalma.simpleweather.domain.repository.CityRepository
import javax.inject.Inject

class CityRepositoryImpl @Inject constructor(
    private val api: GeocodingApi,
    private val cityDao: CityDao
) : CityRepository {
    override suspend fun searchCityByName(
        name: String
    ): Result<List<CityInfo>> {
        return runCatching { api.searchCity(name).toDomain() }
    }

    override suspend fun saveCity(cityInfo: CityInfo): Result<Unit> {
        return runCatching { cityDao.upsert(cityInfo.toEntity()) }
    }

    override suspend fun deleteCity(cityInfo: CityInfo): Result<Unit> {
        return runCatching { cityDao.delete(cityInfo.id) }
    }

    override suspend fun getCities(): Result<List<CityInfo>> {
        return runCatching { cityEntityListToCityInfoList(cityDao.getAllCities()) }
    }
}