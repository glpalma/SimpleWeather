package com.glpalma.simpleweather.ui.weather

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.glpalma.simpleweather.domain.model.CurrentWeather
import com.glpalma.simpleweather.domain.model.WeatherCondition
import com.glpalma.simpleweather.domain.model.WeatherReport

@Composable
fun WeatherRoute(
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(key1 = Unit) {
        viewModel.loadWeatherFromName(
            name = "Campinas"
        )
    }

    WeatherScreen(state)
}

@Composable
fun WeatherScreen(
    state: WeatherUiState
) {
    when (state) {
        WeatherUiState.Loading -> {
            Text(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                text = "Loading...",
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                textAlign = TextAlign.Center,

                )
        }

        is WeatherUiState.Success -> {
            val report = state.report

            Column(Modifier.fillMaxSize()) {
                Row(Modifier.fillMaxSize()) {
                    Text(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        text = "Campinas",
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        textAlign = TextAlign.Center,
                        color = Color.Blue
                    )
                    Text(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        text = report.current.temperatureC.toString(),
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        textAlign = TextAlign.Center,
                        color = Color.Blue
                    )
                }
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize(),
                    text = "Temperature: ${report.current.temperatureC}",
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    textAlign = TextAlign.Center
                )
            }

        }

        is WeatherUiState.Error -> {
            Text(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                text = "Error: ${state.message}",
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WeatherScreenSuccessPreview() {
    WeatherScreen(
        state = WeatherUiState.Success(report = fakeWeatherReport())
    )
}

@Preview(showBackground = true)
@Composable
fun WeatherScreenLoadingPreview() {
    WeatherScreen(
        state = WeatherUiState.Loading
    )
}

@Preview(showBackground = true)
@Composable
fun WeatherScreenErrorPreview() {
    WeatherScreen(
        state = WeatherUiState.Error("Network error")
    )
}

fun fakeWeatherReport() = WeatherReport(
    current = CurrentWeather(
        temperatureC = 27.0,
        condition = WeatherCondition.CLEAR
    ),
    daily = emptyList()
)