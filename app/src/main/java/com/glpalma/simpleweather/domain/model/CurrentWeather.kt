package com.glpalma.simpleweather.domain.model

data class CurrentWeather(
    val temperatureC: Double,
    val condition: WeatherCondition
)