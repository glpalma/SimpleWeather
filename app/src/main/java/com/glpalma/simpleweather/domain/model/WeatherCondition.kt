package com.glpalma.simpleweather.domain.model

enum class WeatherCondition(val displayName: String) {
    CLEAR("Clear"),
    PARTLY_CLOUDY("Partly Cloudy"),
    CLOUDY("Cloudy"),
    FOG("Fog"),
    DRIZZLE("Drizzle"),
    RAIN("Rain"),
    STORM("Storm"),
    SNOW("Snow"),
    UNKNOWN("Unknown")
}
