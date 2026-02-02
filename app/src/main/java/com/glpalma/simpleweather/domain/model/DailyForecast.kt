package com.glpalma.simpleweather.domain.model

import java.time.LocalDate

data class DailyForecast(
    val date: LocalDate,
    val minTempC: Double,
    val maxTempC: Double,
    val condition: WeatherCondition
)
