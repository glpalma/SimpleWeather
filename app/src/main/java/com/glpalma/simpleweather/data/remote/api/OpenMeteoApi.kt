package com.glpalma.simpleweather.data.remote.api

import com.glpalma.simpleweather.data.remote.ForecastDto
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenMeteoApi {

    @GET("v1/forecast")
    suspend fun getForecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current")
        current: String = "temperature_2m,weather_code",
        @Query("daily")
        daily: String = "temperature_2m_max,temperature_2m_min,weather_code",
        @Query("forecast_days")
        days: Int = 7,
        @Query("temperature_unit")
        tempUnit: String = "celsius",
        @Query("timezone")
        timezone: String = "auto"
    ): ForecastDto
}