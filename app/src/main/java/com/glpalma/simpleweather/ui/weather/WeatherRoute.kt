package com.glpalma.simpleweather.ui.weather

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.glpalma.simpleweather.domain.model.CityInfo
import com.glpalma.simpleweather.domain.model.CurrentWeather
import com.glpalma.simpleweather.domain.model.DailyForecast
import com.glpalma.simpleweather.domain.model.DisplayInfo
import com.glpalma.simpleweather.domain.model.HourlyForecast
import com.glpalma.simpleweather.domain.model.WeatherCondition
import com.glpalma.simpleweather.domain.model.WeatherReport
import com.glpalma.simpleweather.ui.RoundButton
import com.glpalma.simpleweather.ui.theme.SimpleWeatherTheme
import com.glpalma.simpleweather.ui.theme.WeatherBlack
import com.glpalma.simpleweather.ui.weather.components.CityPickerContent
import com.glpalma.simpleweather.ui.weather.components.CompactTomorrowContent
import com.glpalma.simpleweather.ui.weather.components.HourlyForecastCard
import com.glpalma.simpleweather.ui.weather.components.TodayCardContent
import com.glpalma.simpleweather.ui.weather.components.WeatherCard
import com.glpalma.simpleweather.ui.weather.components.WeatherTopBar
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.math.roundToInt

@Composable
fun WeatherRoute(
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    BackHandler(enabled = state.screenMode != WeatherScreenMode.TODAY) {
        viewModel.setScreenMode(WeatherScreenMode.TODAY)
    }

    WeatherScreen(
        state = state,
        onGridClick = { viewModel.setScreenMode(WeatherScreenMode.CITY_PICKER) },
        onBackClick = { viewModel.setScreenMode(WeatherScreenMode.TODAY) },
        onSevenDaysClick = { viewModel.setScreenMode(WeatherScreenMode.SEVEN_DAY) },
        onRefresh = viewModel::refreshCurrentCity,
        onSavedCitySelected = viewModel::selectSavedCity,
        onNewCitySelected = viewModel::selectAndSaveNewCity,
        onSearchQueryChange = viewModel::updateSearchQuery,
        onToggleCitySearch = viewModel::toggleCitySearch,
        onClosePicker = { viewModel.setScreenMode(WeatherScreenMode.TODAY) },
        onCloseSevenDay = { viewModel.setScreenMode(WeatherScreenMode.TODAY) })
}

@Composable
fun WeatherScreen(
    state: WeatherUiState,
    onGridClick: () -> Unit,
    onBackClick: () -> Unit,
    onSevenDaysClick: () -> Unit,
    onRefresh: () -> Unit,
    onSavedCitySelected: (DisplayInfo) -> Unit,
    onNewCitySelected: (CityInfo) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onToggleCitySearch: () -> Unit,
    onClosePicker: () -> Unit,
    onCloseSevenDay: () -> Unit,

    ) {
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()
    val pullOffset = remember { Animatable(0f) }
    val refreshThresholdPx = with(density) { 120.dp.toPx() }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPostScroll(
                consumed: Offset, available: Offset, source: NestedScrollSource
            ): Offset {
                if (available.y > 0 && source == NestedScrollSource.UserInput) {
                    val dampened = available.y * 0.5f
                    scope.launch {
                        pullOffset.snapTo(
                            (pullOffset.value + dampened).coerceAtMost(refreshThresholdPx * 1.3f)
                        )
                    }
                    return Offset(0f, available.y)
                }
                return Offset.Zero
            }

            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (available.y < 0 && pullOffset.value > 0f) {
                    val consumed = available.y.coerceAtLeast(-pullOffset.value)
                    scope.launch {
                        pullOffset.snapTo((pullOffset.value + consumed).coerceAtLeast(0f))
                    }
                    return Offset(0f, consumed)
                }
                return Offset.Zero
            }

            override suspend fun onPreFling(available: Velocity): Velocity {
                if (pullOffset.value >= refreshThresholdPx) {
                    onRefresh()
                }
                pullOffset.animateTo(
                    0f, spring(stiffness = Spring.StiffnessMedium)
                )
                return available
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WeatherBlack)
    ) {
        val canPullToRefresh = state.screenMode != WeatherScreenMode.CITY_PICKER

        Column(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (canPullToRefresh) {
                    Modifier.nestedScroll(nestedScrollConnection)
                        .offset { IntOffset(0, pullOffset.value.roundToInt()) }
                } else {
                    Modifier
                })) {

            WeatherCard(mode = state.screenMode) {
                Column {
                    Spacer(Modifier.statusBarsPadding())

                    when (state.screenMode) {
                        WeatherScreenMode.TODAY -> WeatherTopBar(leadingAction = {
                            RoundButton(
                                onClick = onGridClick,
                                imageVector = Icons.Filled.Apps,
                                contentDescription = "City selection"
                            )
                        }, title = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Filled.LocationOn,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    text = state.currentCity?.name ?: "",
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 24.sp
                                )
                            }
                        }, trailingAction = {
                            RoundButton(
                                onClick = onRefresh,
                                imageVector = Icons.Filled.Update,
                                contentDescription = "Refresh"
                            )
                        })

                        WeatherScreenMode.SEVEN_DAY -> WeatherTopBar(leadingAction = {
                            RoundButton(
                                onClick = onBackClick,
                                imageVector = Icons.Filled.ArrowBackIosNew,
                                contentDescription = "Back"
                            )
                        }, title = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Filled.CalendarMonth,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    text = "7 days",
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 16.sp
                                )
                            }
                        })

                        WeatherScreenMode.CITY_PICKER -> WeatherTopBar(leadingAction = {
                            RoundButton(
                                onClick = onClosePicker,
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Close"
                            )
                        }, trailingAction = {
                            RoundButton(
                                onClick = onToggleCitySearch,
                                imageVector = Icons.Filled.Search,
                                contentDescription = "Search"
                            )
                        })
                    }

                    AnimatedContent(
                        targetState = state.screenMode, transitionSpec = {
                            fadeIn(animationSpec = androidx.compose.animation.core.tween(300)) togetherWith fadeOut(
                                animationSpec = androidx.compose.animation.core.tween(300)
                            )
                        }, label = "cardContent"
                    ) { targetMode ->
                        when (targetMode) {
                            WeatherScreenMode.TODAY -> {
                                Column {
                                    TodayCardContent(
                                        currentWeather = state.currentWeather,
                                        isRefreshing = state.isRefreshing
                                    )

                                }
                            }

                            WeatherScreenMode.SEVEN_DAY -> {
                                CompactTomorrowContent(
                                    dailyForecasts = state.currentWeather?.daily ?: emptyList(),
                                    onClose = onCloseSevenDay
                                )
                            }

                            WeatherScreenMode.CITY_PICKER -> {
                                CityPickerContent(
                                    savedCities = state.savedCitiesWithWeather,
                                    searchQuery = state.searchQuery,
                                    searchResults = state.searchResults,
                                    isSearching = state.isSearching,
                                    isCitySearchVisible = state.isCitySearchVisible,
                                    onSearchQueryChange = onSearchQueryChange,
                                    onSavedCitySelected = onSavedCitySelected,
                                    onNewCitySelected = onNewCitySelected
                                )
                            }
                        }
                    }
                }
            }

            AnimatedContent(
                targetState = state.screenMode, transitionSpec = {
                    fadeIn(animationSpec = androidx.compose.animation.core.tween(300)) togetherWith fadeOut(
                        animationSpec = androidx.compose.animation.core.tween(300)
                    )
                }, label = "bottomContent"
            ) { targetMode ->
                when (targetMode) {
                    WeatherScreenMode.TODAY -> {
                        TodayBottomSection(
                            hourlyForecasts = state.currentWeather?.hourly ?: emptyList(),
                            onSevenDaysClick = onSevenDaysClick
                        )
                    }

                    WeatherScreenMode.SEVEN_DAY -> {
                        SevenDayBottomSection(
                            dailyForecasts = state.currentWeather?.daily ?: emptyList()
                        )
                    }

                    WeatherScreenMode.CITY_PICKER -> {
                        Spacer(Modifier.height(0.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun TodayBottomSection(
    hourlyForecasts: List<HourlyForecast>, onSevenDaysClick: () -> Unit
) {
    val now = LocalDateTime.now()
    val relevantHours = filterRelevantHours(hourlyForecasts, now)

    Column(
        modifier = Modifier
            .padding(top = 16.dp)
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Today", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp
            )
            TextButton(onClick = onSevenDaysClick) {
                Text(
                    text = "7 days >", color = Color.White.copy(alpha = 0.5f), fontSize = 14.sp
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        LazyRow(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            items(relevantHours, key = { it.time.toString() }) { forecast ->
                HourlyForecastCard(
                    forecast = forecast,
                    isCurrentHour = forecast.time.hour == now.hour && forecast.time.toLocalDate() == now.toLocalDate()
                )
            }
        }
    }
}

@Composable
private fun SevenDayBottomSection(
    dailyForecasts: List<DailyForecast>
) {
    Column(
        modifier = Modifier
            .padding(top = 16.dp)
            .navigationBarsPadding()
    ) {
        com.glpalma.simpleweather.ui.weather.components.SevenDayList(
            dailyForecasts = dailyForecasts
        )
    }
}

private fun filterRelevantHours(
    hourly: List<HourlyForecast>, now: LocalDateTime
): List<HourlyForecast> {
    val currentHourIndex = hourly.indexOfFirst {
        it.time.hour == now.hour && it.time.toLocalDate() == now.toLocalDate()
    }
    if (currentHourIndex == -1) return hourly.take(4)

    val startIndex = (currentHourIndex - 1).coerceAtLeast(0)
    val endIndex = (currentHourIndex + 3).coerceAtMost(hourly.size)
    return hourly.subList(startIndex, endIndex)
}

// region Previews

private val previewCity = CityInfo(
    name = "São Paulo",
    id = "1",
    latitude = 50.58,
    longitude = 8.67,
    timezone = "America/Sao_Paulo",
    country = "Brazil",
    stateOrProvince = "São Paulo"
)

private val previewHourly = listOf(
    HourlyForecast(
        time = LocalDateTime.now().withMinute(0).minusHours(1),
        temperatureC = 23.0,
        condition = WeatherCondition.CLOUDY,
        isDay = true
    ), HourlyForecast(
        time = LocalDateTime.now().withMinute(0),
        temperatureC = 21.0,
        condition = WeatherCondition.STORM,
        isDay = true
    ), HourlyForecast(
        time = LocalDateTime.now().withMinute(0).plusHours(1),
        temperatureC = 22.0,
        condition = WeatherCondition.RAIN,
        isDay = true
    ), HourlyForecast(
        time = LocalDateTime.now().withMinute(0).plusHours(2),
        temperatureC = 19.0,
        condition = WeatherCondition.SNOW,
        isDay = false
    )
)

private val previewDaily = listOf(
    DailyForecast(LocalDate.now(), 14.0, 20.0, WeatherCondition.RAIN),
    DailyForecast(LocalDate.now().plusDays(1), 16.0, 22.0, WeatherCondition.RAIN),
    DailyForecast(LocalDate.now().plusDays(2), 13.0, 19.0, WeatherCondition.STORM),
    DailyForecast(LocalDate.now().plusDays(3), 12.0, 18.0, WeatherCondition.CLOUDY),
    DailyForecast(LocalDate.now().plusDays(4), 19.0, 23.0, WeatherCondition.STORM),
    DailyForecast(LocalDate.now().plusDays(5), 17.0, 25.0, WeatherCondition.RAIN),
    DailyForecast(LocalDate.now().plusDays(6), 18.0, 21.0, WeatherCondition.STORM)
)

private val previewWeather = WeatherReport(
    current = CurrentWeather(
        temperatureC = 21.0, condition = WeatherCondition.STORM, isDay = true
    ), hourly = previewHourly, daily = previewDaily
)

private val previewState = WeatherUiState(
    screenMode = WeatherScreenMode.TODAY,
    currentCity = previewCity,
    currentWeather = previewWeather,
    isRefreshing = true
)

private val noopCallbacks: Map<String, () -> Unit> = emptyMap()

@Preview(showBackground = true, showSystemUi = true, name = "Today")
@Composable
private fun WeatherScreenTodayPreview() {
    SimpleWeatherTheme(darkTheme = true, dynamicColor = false) {
        WeatherScreen(
            state = previewState,
            onGridClick = {},
            onBackClick = {},
            onSevenDaysClick = {},
            onRefresh = {},
            onSavedCitySelected = {},
            onNewCitySelected = {},
            onSearchQueryChange = {},
            onToggleCitySearch = {},
            onClosePicker = {},
            onCloseSevenDay = {})
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "7 Days")
@Composable
private fun WeatherScreenSevenDayPreview() {
    SimpleWeatherTheme(darkTheme = true, dynamicColor = false) {
        WeatherScreen(
            state = previewState.copy(screenMode = WeatherScreenMode.SEVEN_DAY),
            onGridClick = {},
            onBackClick = {},
            onSevenDaysClick = {},
            onRefresh = {},
            onSavedCitySelected = {},
            onNewCitySelected = {},
            onSearchQueryChange = {},
            onToggleCitySearch = {},
            onClosePicker = {},
            onCloseSevenDay = {})
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "City Picker")
@Composable
private fun WeatherScreenCityPickerPreview() {
    SimpleWeatherTheme(darkTheme = true, dynamicColor = false) {
        WeatherScreen(
            state = previewState.copy(
            screenMode = WeatherScreenMode.CITY_PICKER, savedCitiesWithWeather = listOf(
                DisplayInfo(previewCity, previewWeather), DisplayInfo(
                    previewCity.copy(
                        id = "2", name = "São Paulo", country = "Brazil", stateOrProvince = "SP"
                    ), previewWeather.copy(
                        current = CurrentWeather(27.0, WeatherCondition.CLEAR, true)
                    )
                )
            )
        ),
            onGridClick = {},
            onBackClick = {},
            onSevenDaysClick = {},
            onRefresh = {},
            onSavedCitySelected = {},
            onNewCitySelected = {},
            onSearchQueryChange = {},
            onToggleCitySearch = {},
            onClosePicker = {},
            onCloseSevenDay = {})
    }
}

// endregion
