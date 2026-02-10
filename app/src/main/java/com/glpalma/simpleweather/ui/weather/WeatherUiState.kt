package com.glpalma.simpleweather.ui.weather

import com.glpalma.simpleweather.domain.model.CityInfo
import com.glpalma.simpleweather.domain.model.DisplayInfo

data class WeatherUiState(
    val searchQuery: String = "",
    val searchResults: List<CityInfo> = emptyList(),
    val isSearching: Boolean = false,
    val savedCitiesWithWeather: List<DisplayInfo> = emptyList(),
    val isLoadingSaved: Boolean = false,
    val errorMessage: String? = null
)
