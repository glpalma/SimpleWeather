package com.glpalma.simpleweather.domain.model

data class CityInfo(
    val name: String,
    val id: String,
    val latitude: Double,
    val longitude: Double,
    val elevation: Double,
    val timezone: String,
    val country: String,
    val stateOrProvince: String
)
