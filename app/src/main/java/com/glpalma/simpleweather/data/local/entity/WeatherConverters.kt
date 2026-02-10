package com.glpalma.simpleweather.data.local.entity

import androidx.room.TypeConverter
import com.google.gson.Gson

class WeatherConverters {

    private val gson = Gson()

    @TypeConverter
    fun fromStringList(value: List<String>): String =
        gson.toJson(value)

    @TypeConverter
    fun toStringList(value: String): List<String> =
        gson.fromJson(value, Array<String>::class.java).toList()

    @TypeConverter
    fun fromDoubleList(value: List<Double>): String =
        gson.toJson(value)

    @TypeConverter
    fun toDoubleList(value: String): List<Double> =
        gson.fromJson(value, Array<Double>::class.java).toList()

    @TypeConverter
    fun fromIntList(value: List<Int>): String =
        gson.toJson(value)

    @TypeConverter
    fun toIntList(value: String): List<Int> =
        gson.fromJson(value, Array<Int>::class.java).toList()
}