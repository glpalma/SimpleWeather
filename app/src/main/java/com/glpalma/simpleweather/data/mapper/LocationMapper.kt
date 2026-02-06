package com.glpalma.simpleweather.data.mapper

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
                elevation = item.elevation,
                timezone = item.timezone,
                country = item.country,
                stateOrProvince = item.stateOrProvince
            )
        )
    }

    return list
}