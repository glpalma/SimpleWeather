package com.glpalma.simpleweather.data.mapper

import com.glpalma.simpleweather.domain.model.WeatherCondition

fun weatherCodeToCondition(code: Int): WeatherCondition =
    when (code) {
        0 -> WeatherCondition.CLEAR
        1, 2 -> WeatherCondition.PARTLY_CLOUDY
        3 -> WeatherCondition.CLOUDY
        45, 48 -> WeatherCondition.FOG
        in 51..57 -> WeatherCondition.DRIZZLE
        in 61..67 -> WeatherCondition.RAIN
        in 71..77 -> WeatherCondition.SNOW
        in 80..82 -> WeatherCondition.RAIN
        95, 96, 99 -> WeatherCondition.STORM
        else -> WeatherCondition.UNKNOWN
    }
