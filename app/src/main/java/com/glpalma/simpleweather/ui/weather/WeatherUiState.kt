package com.glpalma.simpleweather.ui.weather

import com.glpalma.simpleweather.domain.model.CityInfo
import com.glpalma.simpleweather.domain.model.DisplayInfo
import com.glpalma.simpleweather.domain.model.WeatherReport

data class WeatherUiState(
    val screenMode: WeatherScreenMode = WeatherScreenMode.TODAY,
    val currentCity: CityInfo? = null,
    val currentWeather: WeatherReport? = null,
    val isRefreshing: Boolean = false,
    val savedCitiesWithWeather: List<DisplayInfo> = emptyList(),
    val searchQuery: String = "",
    val searchResults: List<CityInfo> = emptyList(),
    val isSearching: Boolean = false,
    val isCitySearchVisible: Boolean = false,
    val errorMessage: String? = null
)
