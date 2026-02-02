package com.glpalma.simpleweather.data.mapper

import com.glpalma.simpleweather.data.remote.CurrentDto
import com.glpalma.simpleweather.data.remote.DailyDto
import com.glpalma.simpleweather.data.remote.ForecastDto
import com.glpalma.simpleweather.domain.model.CurrentWeather
import com.glpalma.simpleweather.domain.model.DailyForecast
import com.glpalma.simpleweather.domain.model.WeatherReport
import java.time.LocalDate

fun ForecastDto.toDomain(): WeatherReport {
    return WeatherReport(
        current = current.toDomain(),
        daily = daily.toDomain()
    )
}

fun CurrentDto.toDomain(): CurrentWeather {
    return CurrentWeather(
        temperatureC = temperature_2m,
        condition = weatherCodeToCondition(weather_code)
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
