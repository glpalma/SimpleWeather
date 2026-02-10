package com.glpalma.simpleweather.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "weather",
    foreignKeys = [
        ForeignKey(
            entity = CityEntity::class,
            parentColumns = ["cityId"],
            childColumns = ["cityId"],
            onDelete = ForeignKey.CASCADE
        )
])
data class WeatherEntity(
    @PrimaryKey val cityId: String,
    val currentTemp: Double,
    val lastUpdated: Long,
    val currentWeatherCode: Int,

    val dates: List<String>,
    val maxTemps: List<Double>,
    val minTemps: List<Double>,
    val weatherCodes: List<Int>
)
