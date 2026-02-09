package com.glpalma.simpleweather.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.glpalma.simpleweather.data.local.entity.CityEntity

@Dao
interface CityDao {

    @Query("SELECT * FROM cities WHERE cityId = :cityId")
    suspend fun getCity(cityId: String): CityEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(city: CityEntity)

    @Query("DELETE FROM cities WHERE cityId = :cityId")
    suspend fun delete(cityId: String)

    @Query("SELECT * FROM cities")
    suspend fun getAllCities(): List<CityEntity>
}