package com.glpalma.simpleweather.ui.weather.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.glpalma.simpleweather.domain.model.HourlyForecast
import com.glpalma.simpleweather.ui.theme.HourlyCardBackground
import com.glpalma.simpleweather.ui.theme.HourlyCardSelected
import java.time.format.DateTimeFormatter

@Composable
fun HourlyForecastCard(
    forecast: HourlyForecast,
    isCurrentHour: Boolean,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isCurrentHour) HourlyCardSelected else HourlyCardBackground

    Column(
        modifier = modifier
            .width(72.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(backgroundColor)
            .padding(horizontal = 12.dp, vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "${forecast.temperatureC.toInt()}°",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        Spacer(Modifier.height(8.dp))

        WeatherIcon(
            condition = forecast.condition,
            isDay = forecast.isDay,
            modifier = Modifier.size(28.dp)
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = forecast.time.format(DateTimeFormatter.ofPattern("HH:mm")),
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 12.sp
        )
    }
}
