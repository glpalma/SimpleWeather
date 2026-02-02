package com.glpalma.simpleweather.domain.model

data class WeatherReport(
    val current: CurrentWeather,
    val daily: List<DailyForecast>
)