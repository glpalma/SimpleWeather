package com.glpalma.simpleweather.ui.weather.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.glpalma.simpleweather.domain.model.DailyForecast
import com.glpalma.simpleweather.ui.theme.Rubik

@Composable
fun CompactTomorrowContent(
    dailyForecasts: List<DailyForecast>, modifier: Modifier = Modifier, onClose: () -> Unit
) {
    val tomorrow = dailyForecasts.getOrNull(1)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Tomorrow", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium
        )

        Spacer(Modifier.height(12.dp))

        if (tomorrow != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                WeatherIcon(
                    condition = tomorrow.condition, isDay = true, modifier = Modifier.size(48.dp)
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    text = "${tomorrow.maxTempC.toInt()}°",
                    fontSize = 64.sp,
                    color = Color.White,
                    fontFamily = Rubik
                )
                Text(
                    text = "/${tomorrow.minTempC.toInt()}°",
                    fontSize = 32.sp,
                    color = Color.White.copy(alpha = 0.7f),
                    fontFamily = Rubik
                )
            }

            Spacer(Modifier.height(4.dp))

            Text(
                text = tomorrow.condition.displayName,
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp
            )
        }
    }
}
