package com.glpalma.simpleweather.ui.weather

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.glpalma.simpleweather.domain.model.CityInfo
import com.glpalma.simpleweather.domain.model.CurrentWeather
import com.glpalma.simpleweather.domain.model.DisplayInfo
import com.glpalma.simpleweather.domain.model.WeatherCondition
import com.glpalma.simpleweather.domain.model.WeatherReport

@Composable
fun WeatherRoute(
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let { _ ->
            // Could show Snackbar here; for now UI shows error text
        }
    }

    WeatherScreen(
        state = state,
        onSearchQueryChange = viewModel::updateSearchQuery,
        onCitySelected = viewModel::selectAndSaveCity,
        onRefreshSaved = viewModel::loadSavedCitiesWithWeather
    )
}

@Composable
fun WeatherScreen(
    state: WeatherUiState,
    onSearchQueryChange: (String) -> Unit,
    onCitySelected: (CityInfo) -> Unit,
    onRefreshSaved: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Search bar
        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Search city") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Search results (matching cities)
        if (state.isSearching) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (state.searchResults.isNotEmpty()) {
            Text(
                text = "Select a city to add",
                modifier = Modifier.padding(vertical = 4.dp),
                fontWeight = FontWeight.Medium
            )
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(
                    state.searchResults,
                    key = { it.id }
                ) { city ->
                    SearchResultItem(
                        city = city,
                        onClick = { onCitySelected(city) }
                    )
                }
            }
        } else {
            Spacer(modifier = Modifier.weight(1f))
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        // Saved cities with current forecast
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Saved cities",
                modifier = Modifier.padding(vertical = 4.dp),
                fontWeight = FontWeight.Bold
            )
            TextButton(onClick = onRefreshSaved) {
                Text("Refresh")
            }
        }
        if (state.isLoadingSaved) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (state.savedCitiesWithWeather.isEmpty()) {
            Text(
                text = "No saved cities. Search and select a city above.",
                modifier = Modifier.padding(16.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    state.savedCitiesWithWeather,
                    key = { it.cityInfo.id }
                ) { displayInfo ->
                    SavedCityWeatherItem(displayInfo = displayInfo)
                }
            }
        }

        state.errorMessage?.let { message ->
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                modifier = Modifier.fillMaxWidth(),
                color = androidx.compose.ui.graphics.Color.Red
            )
        }
    }
}

@Composable
private fun SearchResultItem(
    city: CityInfo,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors()
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = city.name,
                fontWeight = FontWeight.SemiBold
            )
            if (city.stateOrProvince.isNotEmpty()) {
                Text(
                    text = "${city.stateOrProvince}, ${city.country}",
                    style = MaterialTheme.typography.bodySmall
                )
            } else {
                Text(
                    text = city.country,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun SavedCityWeatherItem(
    displayInfo: DisplayInfo
) {
    val city = displayInfo.cityInfo
    val current = displayInfo.report.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = city.name,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = if (city.stateOrProvince.isNotEmpty()) {
                        "${city.stateOrProvince}, ${city.country}"
                    } else {
                        city.country
                    },
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${current.temperatureC.toInt()}°C",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = current.condition.name.replace('_', ' '),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WeatherScreenPreview() {
    WeatherScreen(
        state = WeatherUiState(
            searchQuery = "Camp",
            searchResults = listOf(
                CityInfo(
                    name = "Campinas",
                    id = "1",
                    latitude = -22.9,
                    longitude = -47.1,
                    timezone = "America/Sao_Paulo",
                    country = "Brazil",
                    stateOrProvince = "São Paulo"
                )
            ),
            savedCitiesWithWeather = listOf(
                DisplayInfo(
                    cityInfo = CityInfo(
                        name = "Campinas",
                        id = "1",
                        latitude = -22.9,
                        longitude = -47.1,
                        timezone = "America/Sao_Paulo",
                        country = "Brazil",
                        stateOrProvince = "São Paulo"
                    ),
                    report = WeatherReport(
                        current = CurrentWeather(
                            temperatureC = 27.0,
                            condition = WeatherCondition.CLEAR
                        ),
                        daily = emptyList()
                    )
                )
            )
        ),
        onSearchQueryChange = {},
        onCitySelected = {},
        onRefreshSaved = {}
    )
}
