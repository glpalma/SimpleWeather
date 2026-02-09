package com.glpalma.simpleweather.data.mapper

import com.glpalma.simpleweather.data.local.entity.CityEntity
import com.glpalma.simpleweather.data.remote.CityListDto
import com.glpalma.simpleweather.domain.model.CityInfo

fun CityListDto.toDomain(): List<CityInfo> {
    val list = arrayListOf<CityInfo>()

    for (item in cities) {
        list.add(
            CityInfo(
                name = item.name,
                id = item.id.toString(),
                latitude = item.latitude,
                longitude = item.longitude,
                timezone = item.timezone,
                country = item.country,
                stateOrProvince = item.stateOrProvince
            )
        )
    }

    return list
}

fun CityInfo.toEntity(): CityEntity {
    return CityEntity(
        name = name,
        latitude = latitude,
        longitude = longitude,
        cityId = id,
        country = country,
        stateOrProvince = stateOrProvince,
        timezone = timezone
    )
}

fun cityEntityListToCityInfoList(entities: List<CityEntity>): List<CityInfo> {
    val list = arrayListOf<CityInfo>()

    for (item in entities) {
        list.add(
            CityInfo(
                name = item.name,
                id = item.cityId,
                latitude = item.latitude,
                longitude = item.longitude,
                timezone = item.timezone,
                country = item.country,
                stateOrProvince = item.stateOrProvince
            )
        )
    }

    return list
}