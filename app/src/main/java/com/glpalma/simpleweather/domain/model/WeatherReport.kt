package com.glpalma.simpleweather.domain.model

data class WeatherReport(
    val current: CurrentWeather,
    val hourly: List<HourlyForecast>,
    val daily: List<DailyForecast>
)