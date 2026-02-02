package com.glpalma.simpleweather.data.remote

data class ForecastDto(
    val current: CurrentDto,
    val daily: DailyDto
)