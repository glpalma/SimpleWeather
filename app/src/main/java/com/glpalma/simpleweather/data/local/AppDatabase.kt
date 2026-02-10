package com.glpalma.simpleweather.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.glpalma.simpleweather.data.local.dao.CityDao
import com.glpalma.simpleweather.data.local.dao.WeatherDao
import com.glpalma.simpleweather.data.local.entity.CityEntity
import com.glpalma.simpleweather.data.local.entity.WeatherConverters
import com.glpalma.simpleweather.data.local.entity.WeatherEntity

@Database(
    entities = [
        CityEntity::class,
        WeatherEntity::class
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(WeatherConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao

    abstract fun cityDao(): CityDao
}
