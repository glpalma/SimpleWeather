package com.glpalma.simpleweather.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cities")
data class CityEntity(
    @PrimaryKey val cityId: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val country: String,
    val stateOrProvince: String,
    val timezone: String
)
