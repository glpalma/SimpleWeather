package com.glpalma.simpleweather.domain.model

import java.time.LocalDateTime

data class HourlyForecast(
    val time: LocalDateTime,
    val temperatureC: Double,
    val condition: WeatherCondition,
    val isDay: Boolean = true
)
