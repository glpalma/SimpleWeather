package com.glpalma.simpleweather.ui.weather.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.glpalma.simpleweather.domain.model.DailyForecast
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun SevenDayList(
    dailyForecasts: List<DailyForecast>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(dailyForecasts) { forecast ->
            DailyForecastRow(forecast)
        }
    }
}

@Composable
private fun DailyForecastRow(forecast: DailyForecast) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = forecast.date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
            color = Color.White,
            fontSize = 15.sp,
            modifier = Modifier.width(44.dp)
        )

        WeatherIcon(
            condition = forecast.condition,
            isDay = true,
            modifier = Modifier.size(28.dp)
        )

        Text(
            text = forecast.condition.displayName,
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = "+${forecast.maxTempC.toInt()}°",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )

        Spacer(Modifier.width(8.dp))

        Text(
            text = "+${forecast.minTempC.toInt()}°",
            color = Color.White.copy(alpha = 0.6f),
            fontSize = 15.sp
        )
    }
}
