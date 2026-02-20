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

private fun isDay(hour: Int): Boolean = hour in 6..17

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
    val parsedTime = LocalDateTime.parse(time)
    return CurrentWeather(
        temperatureC = temperature_2m,
        condition = weatherCodeToCondition(weather_code),
        isDay = isDay(parsedTime.hour)
    )
}

fun DailyDto.toDomain(): List<DailyForecast> {
    return time.indices.map { i ->
        DailyForecast(
            date = LocalDate.parse(time[i]),
            minTempC = temperature_2m_min[i],
            maxTempC = temperature_2m_max[i],
            condition = weatherCodeToCondition(weather_code[i])
        )
    }
}

fun HourlyDto.toDomain(): List<HourlyForecast> {
    return time.indices.map { i ->
        val parsedTime = LocalDateTime.parse(time[i])
        HourlyForecast(
            time = parsedTime,
            temperatureC = temperature_2m[i],
            condition = weatherCodeToCondition(weather_code[i]),
            isDay = isDay(parsedTime.hour)
        )
    }
}

fun WeatherEntity.toDomain(): WeatherReport {
    val currentTime = LocalDateTime.ofEpochSecond(
        lastUpdated, 0, java.time.ZoneOffset.UTC
    )
    return WeatherReport(
        current = CurrentWeather(
            temperatureC = currentTemp,
            condition = weatherCodeToCondition(currentWeatherCode),
            isDay = isDay(currentTime.hour)
        ),
        daily = dates.indices.map { i ->
            DailyForecast(
                date = LocalDate.parse(dates[i]),
                minTempC = minTemps[i],
                maxTempC = maxTemps[i],
                condition = weatherCodeToCondition(weatherCodes[i])
            )
        },
        hourly = hourlyTimes.indices.map { i ->
            val parsedTime = LocalDateTime.parse(hourlyTimes[i])
            HourlyForecast(
                time = parsedTime,
                temperatureC = hourlyTemps[i],
                condition = weatherCodeToCondition(hourlyWeatherCodes[i]),
                isDay = isDay(parsedTime.hour)
            )
        }
    )
}
