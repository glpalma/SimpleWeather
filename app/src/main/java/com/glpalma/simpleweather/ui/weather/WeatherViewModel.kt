package com.glpalma.simpleweather.ui.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glpalma.simpleweather.domain.usecase.GetWeatherReportUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getWeatherReport: GetWeatherReportUseCase
): ViewModel() {

    private val _state = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val state: StateFlow<WeatherUiState> = _state

    fun loadWeather(lat: Double, lon: Double) {
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