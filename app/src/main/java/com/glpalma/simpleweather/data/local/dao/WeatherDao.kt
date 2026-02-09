package com.glpalma.simpleweather.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.glpalma.simpleweather.data.local.entity.WeatherEntity

@Dao
interface WeatherDao {

    @Query("SELECT * FROM weather WHERE cityId = :cityId")
    suspend fun getWeather(cityId: String): WeatherEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(weather: WeatherEntity)

    @Query("DELETE FROM weather WHERE cityId = :cityId")
    suspend fun deleteForCity(cityId: String)

}