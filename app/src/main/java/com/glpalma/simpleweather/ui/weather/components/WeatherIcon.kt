package com.glpalma.simpleweather.ui.weather.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.FilterDrama
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Thunderstorm
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.glpalma.simpleweather.domain.model.WeatherCondition

fun WeatherCondition.toIcon(isDay: Boolean): ImageVector = when (this) {
    WeatherCondition.CLEAR -> if (isDay) Icons.Filled.WbSunny else Icons.Filled.DarkMode
    WeatherCondition.PARTLY_CLOUDY -> if (isDay) Icons.Filled.FilterDrama else Icons.Filled.DarkMode
    WeatherCondition.CLOUDY -> Icons.Filled.Cloud
    WeatherCondition.FOG -> Icons.Filled.Cloud
    WeatherCondition.DRIZZLE -> Icons.Filled.Grain
    WeatherCondition.RAIN -> Icons.Filled.WaterDrop
    WeatherCondition.STORM -> Icons.Filled.Thunderstorm
    WeatherCondition.SNOW -> Icons.Filled.AcUnit
    WeatherCondition.UNKNOWN -> Icons.Filled.QuestionMark
}

@Composable
fun WeatherIcon(
    condition: WeatherCondition,
    isDay: Boolean,
    modifier: Modifier = Modifier,
    tint: Color = Color.White
) {
    Icon(
        imageVector = condition.toIcon(isDay),
        contentDescription = condition.displayName,
        modifier = modifier,
        tint = tint
    )
}
