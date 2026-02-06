package com.glpalma.simpleweather.data.remote

import com.google.gson.annotations.SerializedName

data class CityDto(
    val name: String,
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val elevation: Double,
    val timezone: String,
    val country: String,

    @SerializedName("admin1")
    val stateOrProvince: String
)
