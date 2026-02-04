package com.glpalma.simpleweather.ui.weather

import com.glpalma.simpleweather.domain.model.WeatherReport

sealed interface WeatherUiState {
    data object Loading : WeatherUiState
    data class Success(val report: WeatherReport) : WeatherUiState
    data class Error(val message: String) : WeatherUiState
}