package com.glpalma.simpleweather.ui.weather.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.RippleConfiguration
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.glpalma.simpleweather.domain.model.CityInfo
import com.glpalma.simpleweather.domain.model.DisplayInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityPickerContent(
    savedCities: List<DisplayInfo>,
    searchQuery: String,
    searchResults: List<CityInfo>,
    isSearching: Boolean,
    isCitySearchVisible: Boolean,
    onSearchQueryChange: (String) -> Unit,
    onSavedCitySelected: (DisplayInfo) -> Unit,
    onNewCitySelected: (CityInfo) -> Unit,
    onCurrentLocationClick: () -> Unit,
    locationPermissionGranted: Boolean,
    isLoadingLocation: Boolean,
    currentLocationWeather: DisplayInfo?,
    modifier: Modifier = Modifier
) {
    val searchFocusRequester = remember { FocusRequester() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        AnimatedContent(
            targetState = isCitySearchVisible,
            transitionSpec = {
                val enterDirection = if (targetState) -1 else 1
                (fadeIn(tween(250, delayMillis = 80)) + slideInVertically(
                    tween(300)
                ) { enterDirection * it / 12 }) togetherWith
                    (fadeOut(tween(200)) + slideOutVertically(
                        tween(250)
                    ) { -enterDirection * it / 12 })
            },
            label = "cityPickerContent"
        ) { showSearch ->
            if (showSearch) {
                Column {
                    LaunchedEffect(Unit) {
                        searchFocusRequester.requestFocus()
                    }

                    TextField(
                        value = searchQuery,
                        onValueChange = onSearchQueryChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(searchFocusRequester),
                        placeholder = {
                            Text("Search city...", color = Color.White.copy(alpha = 0.5f))
                        },
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Color.White,
                            focusedContainerColor = Color.White.copy(alpha = 0.15f),
                            unfocusedContainerColor = Color.White.copy(alpha = 0.1f),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true
                    )

                    Spacer(Modifier.height(8.dp))

                    if (isSearching) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp)
                        }
                    }

                    if (searchResults.isNotEmpty()) {
                        Text(
                            text = "Results",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        LazyColumn(
                            modifier = Modifier.weight(1f, fill = false),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            items(searchResults, key = { it.id }) { city ->
                                SearchResultRow(
                                    city = city, onClick = { onNewCitySelected(city) })
                            }
                        }

                        HorizontalDivider(
                            color = Color.White.copy(alpha = 0.2f),
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    }
                }
            } else {
                Column {
                    CompositionLocalProvider(
                        LocalRippleConfiguration provides RippleConfiguration(
                            color = Color.White, rippleAlpha = RippleAlpha(
                                pressedAlpha = 0.3f,
                                focusedAlpha = 0.12f,
                                draggedAlpha = 0.08f,
                                hoveredAlpha = 0.04f
                            )
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color.White.copy(alpha = 0.2f))
                                .clickable(onClick = onCurrentLocationClick)
                                .border(
                                    width = 1.dp,
                                    Color.White.copy(0.2f),
                                    RoundedCornerShape(20.dp)
                                )
                                .padding(16.dp, 16.dp, 20.dp, 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(modifier = Modifier.weight(1f)) {
                                Icon(
                                    modifier = Modifier
                                        .align(Alignment.CenterVertically)
                                        .height(28.dp)
                                        .width(28.dp),
                                    imageVector = Icons.Filled.LocationOn,
                                    contentDescription = null,
                                    tint = Color.White,
                                )
                                Spacer(Modifier.width(10.dp))

                                Column {
                                    Text(
                                        text = "Current location",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp
                                    )
                                    val subtitle = when {
                                        !locationPermissionGranted -> "Tap to enable"
                                        isLoadingLocation -> "Locating..."
                                        currentLocationWeather != null ->
                                            currentLocationWeather.cityInfo.name

                                        else -> "Tap to load"
                                    }
                                    Text(
                                        text = subtitle,
                                        color = Color.White.copy(alpha = 0.6f),
                                        fontSize = 13.sp
                                    )
                                }
                            }

                            if (isLoadingLocation) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else if (locationPermissionGranted && currentLocationWeather != null) {
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "${currentLocationWeather.report.current.temperatureC.toInt()}°",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp
                                    )
                                    Text(
                                        text = currentLocationWeather.report.current.condition.displayName,
                                        color = Color.White.copy(alpha = 0.6f),
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = "My Cities",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    if (savedCities.isEmpty()) {
                        Text(
                            text = "No saved cities yet. Use the search to add one.",
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 14.sp,
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            items(savedCities, key = { it.cityInfo.id }) { displayInfo ->
                                SavedCityRow(
                                    displayInfo = displayInfo,
                                    onClick = { onSavedCitySelected(displayInfo) })
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchResultRow(
    city: CityInfo, onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = city.name,
                color = Color.White,
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp
            )
            val subtitle = if (city.stateOrProvince.isNotEmpty()) {
                "${city.stateOrProvince}, ${city.country}"
            } else {
                city.country
            }
            Text(
                text = subtitle, color = Color.White.copy(alpha = 0.6f), fontSize = 13.sp
            )
        }
    }
}

@Composable
private fun SavedCityRow(
    displayInfo: DisplayInfo, onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = displayInfo.cityInfo.name,
                color = Color.White,
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp
            )
            val subtitle = if (displayInfo.cityInfo.stateOrProvince.isNotEmpty()) {
                "${displayInfo.cityInfo.stateOrProvince}, ${displayInfo.cityInfo.country}"
            } else {
                displayInfo.cityInfo.country
            }
            Text(
                text = subtitle, color = Color.White.copy(alpha = 0.6f), fontSize = 13.sp
            )
        }

        Spacer(Modifier.width(12.dp))

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "${displayInfo.report.current.temperatureC.toInt()}°",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = displayInfo.report.current.condition.displayName,
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 12.sp
            )
        }
    }
}
