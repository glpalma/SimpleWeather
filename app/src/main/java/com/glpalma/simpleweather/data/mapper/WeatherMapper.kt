package com.glpalma.simpleweather.data.mapper

import com.glpalma.simpleweather.data.local.entity.WeatherEntity
import com.glpalma.simpleweather.data.remote.CurrentDto
import com.glpalma.simpleweather.data.remote.DailyDto
import com.glpalma.simpleweather.data.remote.ForecastDto
import com.glpalma.simpleweather.data.remote.HourlyDto
import com.glpalma.simpleweather.domain.model.CurrentWeather
import com.glpalma.simpleweather.domain.model.DailyForecast
import com.glpalma.simpleweather.domain.model.HourlyForecast
import com.glpalma.simpleweather.domain.model.WeatherReport
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoField

fun ForecastDto.toDomain(): WeatherReport {
    return WeatherReport(
        current = current.toDomain(), daily = daily.toDomain(), hourly = hourly.toDomain()
    )
}

fun ForecastDto.toEntity(cityId: String): WeatherEntity {
    return WeatherEntity(
        cityId = cityId,
        currentTemp = current.temperature_2m,
        currentWeatherCode = current.weather_code,
        lastUpdated = LocalDateTime.parse(current.time).getLong(ChronoField.SECOND_OF_MINUTE),
        dates = daily.time,
        minTemps = daily.temperature_2m_min,
        maxTemps = daily.temperature_2m_max,
        weatherCodes = daily.weather_code,
        hourlyTimes = hourly.time,
        hourlyTemps = hourly.temperature_2m,
        hourlyWeatherCodes = hourly.weather_code
    )
}

fun CurrentDto.toDomain(): CurrentWeather {
    return CurrentWeather(
        temperatureC = temperature_2m, condition = weatherCodeToCondition(weather_code)
    )
}

fun DailyDto.toDomain(): List<DailyForecast> {
    val forecasts = ArrayList<DailyForecast>()

    for (i in 0..<time.size) {
        forecasts.add(
            DailyForecast(
                date = LocalDate.parse(time[i]),
                minTempC = temperature_2m_min[i],
                maxTempC = temperature_2m_max[i],
                condition = weatherCodeToCondition(weather_code[i])
            )
        )
    }

    return forecasts
}

fun HourlyDto.toDomain(): List<HourlyForecast> {
    val forecasts = ArrayList<HourlyForecast>()

    for (i in 0..<time.size) {
        forecasts.add(
            HourlyForecast(
                time = LocalDateTime.parse(time[i]),
                temperatureC = temperature_2m[i],
                condition = weatherCodeToCondition(weather_code[i])
            )
        )
    }

    return forecasts
}

fun WeatherEntity.toDomain(): WeatherReport {
    return WeatherReport(
        current = CurrentDto(
            temperature_2m = currentTemp,
            weather_code = currentWeatherCode,
            time = LocalDateTime.ofEpochSecond(
                lastUpdated,
                0,
                java.time.ZoneOffset.UTC
            ).toString()
        ).toDomain(),
        daily = DailyDto(
            time = dates,
            temperature_2m_min = minTemps,
            temperature_2m_max = maxTemps,
            weather_code = weatherCodes
        ).toDomain(),
        hourly = HourlyDto(
            time = hourlyTimes,
            temperature_2m = hourlyTemps,
            weather_code = hourlyWeatherCodes
        ).toDomain()
    )
}
