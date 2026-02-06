package com.glpalma.simpleweather.data.remote

import com.google.gson.annotations.SerializedName

data class CityListDto(
    @SerializedName("results")
    val cities: List<CityDto>
)
