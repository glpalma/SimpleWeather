package com.glpalma.simpleweather.ui.weather

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.loadWeather(
            lat = -23.55, // São Paulo, hardcoded for now
            lon = -46.63
        )
    }

    val state by viewModel.state.collectAsState()
    when (state) {
        WeatherUiState.Loading -> {
            Text(
                modifier = Modifier.padding(16.dp),
                text = "Loading...",
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                textAlign = TextAlign.Center
            )
        }

        is WeatherUiState.Success -> {
            val report = (state as WeatherUiState.Success).report
            Text(
                modifier = Modifier.padding(16.dp),
                text = "Temperature: ${report.current.temperatureC}",
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                textAlign = TextAlign.Center
            )
        }

        is WeatherUiState.Error -> {
            Text("Error: ${(state as WeatherUiState.Error).message}")
        }
    }
}
