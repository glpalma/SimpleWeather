package com.glpalma.simpleweather.data.remote

data class HourlyDto(
    val time: List<String>,
    val temperature_2m: List<Double>,
    val weather_code: List<Int>
)