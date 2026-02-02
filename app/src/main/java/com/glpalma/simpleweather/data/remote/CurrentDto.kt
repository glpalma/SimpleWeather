package com.glpalma.simpleweather.data.remote

data class CurrentDto(
    val temperature_2m: Double,
    val weather_code: Int,
    val time: String
)
