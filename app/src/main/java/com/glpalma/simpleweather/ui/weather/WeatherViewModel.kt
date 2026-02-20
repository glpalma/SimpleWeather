package com.glpalma.simpleweather.ui.weather

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glpalma.simpleweather.domain.model.CityInfo
import com.glpalma.simpleweather.domain.model.DisplayInfo
import com.glpalma.simpleweather.domain.usecase.GetCityListFromNameUseCase
import com.glpalma.simpleweather.domain.usecase.GetSavedCitiesWithWeatherUseCase
import com.glpalma.simpleweather.domain.usecase.GetWeatherReportUseCase
import com.glpalma.simpleweather.domain.usecase.SaveCityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getCityListFromName: GetCityListFromNameUseCase,
    private val saveCity: SaveCityUseCase,
    private val getSavedCitiesWithWeather: GetSavedCitiesWithWeatherUseCase,
    private val getWeatherReport: GetWeatherReportUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(WeatherUiState())
    val state: StateFlow<WeatherUiState> = _state.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            getSavedCitiesWithWeather()
                .onSuccess { list ->
                    if (list.isNotEmpty()) {
                        val first = list.first()
                        _state.update {
                            it.copy(
                                savedCitiesWithWeather = list,
                                currentCity = first.cityInfo,
                                currentWeather = first.report,
                                screenMode = WeatherScreenMode.TODAY
                            )
                        }
                        refreshCurrentCity()
                    } else {
                        _state.update {
                            it.copy(screenMode = WeatherScreenMode.CITY_PICKER)
                        }
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            screenMode = WeatherScreenMode.CITY_PICKER,
                            errorMessage = error.message
                        )
                    }
                }
        }
    }

    fun setScreenMode(mode: WeatherScreenMode) {
        _state.update {
            it.copy(
                screenMode = mode,
                isCitySearchVisible = false,
                searchQuery = "",
                searchResults = emptyList()
            )
        }
        if (mode == WeatherScreenMode.CITY_PICKER) {
            loadSavedCities()
        }
    }

    fun selectSavedCity(displayInfo: DisplayInfo) {
        _state.update {
            it.copy(
                currentCity = displayInfo.cityInfo,
                currentWeather = displayInfo.report,
                screenMode = WeatherScreenMode.TODAY,
                isCitySearchVisible = false,
                searchQuery = "",
                searchResults = emptyList()
            )
        }
        refreshCurrentCity()
    }

    fun selectAndSaveNewCity(cityInfo: CityInfo) {
        viewModelScope.launch {
            Log.i("WeatherViewModel", "selectAndSaveNewCity: $cityInfo")
            saveCity(cityInfo)
                .onSuccess {
                    _state.update {
                        it.copy(
                            currentCity = cityInfo,
                            currentWeather = null,
                            screenMode = WeatherScreenMode.TODAY,
                            isCitySearchVisible = false,
                            searchQuery = "",
                            searchResults = emptyList()
                        )
                    }
                    refreshCurrentCity()
                }
                .onFailure { error ->
                    Log.e("WeatherViewModel", "failed to save CityInfo: $cityInfo")
                    Log.e("WeatherViewModel", "due to error: $error")
                    _state.update { it.copy(errorMessage = error.message) }
                }
        }
    }

    fun refreshCurrentCity() {
        val city = _state.value.currentCity ?: return
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }
            getWeatherReport(city)
                .onSuccess { report ->
                    _state.update {
                        it.copy(currentWeather = report, isRefreshing = false)
                    }
                    loadSavedCities()
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isRefreshing = false,
                            errorMessage = error.message ?: "Failed to refresh weather"
                        )
                    }
                }
        }
    }

    private fun loadSavedCities() {
        viewModelScope.launch {
            getSavedCitiesWithWeather()
                .onSuccess { list ->
                    _state.update { it.copy(savedCitiesWithWeather = list) }
                }
        }
    }

    fun updateSearchQuery(query: String) {
        _state.update { it.copy(searchQuery = query, errorMessage = null) }
        searchJob?.cancel()
        if (query.isBlank()) {
            _state.update { it.copy(searchResults = emptyList(), isSearching = false) }
            return
        }
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_MS)
            _state.update { it.copy(isSearching = true) }
            getCityListFromName(query)
                .onSuccess { list ->
                    _state.update {
                        it.copy(searchResults = list, isSearching = false)
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            searchResults = emptyList(),
                            isSearching = false,
                            errorMessage = error.message ?: "Search failed"
                        )
                    }
                }
        }
    }

    fun toggleCitySearch() {
        _state.update {
            it.copy(
                isCitySearchVisible = !it.isCitySearchVisible,
                searchQuery = "",
                searchResults = emptyList()
            )
        }
    }

    fun clearError() {
        _state.update { it.copy(errorMessage = null) }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_MS = 300L
    }
}
