package com.glpalma.simpleweather.ui.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glpalma.simpleweather.domain.usecase.GetCityListFromNameUseCase
import com.glpalma.simpleweather.domain.usecase.GetWeatherReportUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getWeatherReport: GetWeatherReportUseCase,
    private val getCities: GetCityListFromNameUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val state: StateFlow<WeatherUiState> = _state

    fun loadWeatherFromName(name: String) {
        viewModelScope.launch {
            _state.value = WeatherUiState.Loading

            getCities(name)
                .onSuccess { list ->
                    getWeatherReport(list[0].latitude, list[0].longitude)
                        .onSuccess { report ->
                            _state.value = WeatherUiState.Success(report)
                        }
                        .onFailure { error ->
                            _state.value = WeatherUiState.Error(error.message ?: "Unknown error")
                        }
                }
                .onFailure { error ->
                    _state.value = WeatherUiState.Error(error.message ?: "Unknown error")
                }
        }
    }

    private fun loadWeather(
        lat: Double,
        lon: Double
    ) { // TODO: remove after UI has been fully implemented
        viewModelScope.launch {
            _state.value = WeatherUiState.Loading

            getWeatherReport(lat, lon)
                .onSuccess { report ->
                    _state.value = WeatherUiState.Success(report)
                }
                .onFailure { error ->
                    _state.value = WeatherUiState.Error(error.message ?: "Unknown error")

                }
        }
    }
}