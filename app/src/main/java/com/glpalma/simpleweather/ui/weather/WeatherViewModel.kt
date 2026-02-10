package com.glpalma.simpleweather.ui.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glpalma.simpleweather.domain.model.CityInfo
import com.glpalma.simpleweather.domain.usecase.GetCityListFromNameUseCase
import com.glpalma.simpleweather.domain.usecase.GetSavedCitiesWithWeatherUseCase
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
    private val getSavedCitiesWithWeather: GetSavedCitiesWithWeatherUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(WeatherUiState())
    val state: StateFlow<WeatherUiState> = _state.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadSavedCitiesWithWeather()
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
                        it.copy(
                            searchResults = list,
                            isSearching = false
                        )
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

    fun selectAndSaveCity(cityInfo: CityInfo) {
        viewModelScope.launch {
            saveCity(cityInfo)
                .onSuccess {
                    _state.update {
                        it.copy(
                            searchQuery = "",
                            searchResults = emptyList(),
                            errorMessage = null
                        )
                    }
                    loadSavedCitiesWithWeather()
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(errorMessage = error.message ?: "Failed to save city")
                    }
                }
        }
    }

    fun loadSavedCitiesWithWeather() {
        viewModelScope.launch {
            _state.update { it.copy(isLoadingSaved = true, errorMessage = null) }
            getSavedCitiesWithWeather()
                .onSuccess { list ->
                    _state.update {
                        it.copy(
                            savedCitiesWithWeather = list,
                            isLoadingSaved = false
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoadingSaved = false,
                            errorMessage = error.message ?: "Failed to load saved cities"
                        )
                    }
                }
        }
    }

    fun clearError() {
        _state.update { it.copy(errorMessage = null) }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_MS = 300L
    }
}
