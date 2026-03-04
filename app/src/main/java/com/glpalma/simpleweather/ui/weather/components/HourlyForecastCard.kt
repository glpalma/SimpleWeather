package com.glpalma.simpleweather.ui.weather.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.glpalma.simpleweather.domain.model.HourlyForecast
import com.glpalma.simpleweather.ui.theme.HourlyCardBackground
import com.glpalma.simpleweather.ui.theme.HourlyCardBorder
import com.glpalma.simpleweather.ui.theme.HourlyCardSelected
import java.time.format.DateTimeFormatter

const val defaultCardWidth = 75

@Composable
fun HourlyForecastCard(
    forecast: HourlyForecast, isCurrentHour: Boolean, modifier: Modifier = Modifier
) {
    val backgroundColor = if (isCurrentHour) HourlyCardSelected else HourlyCardBackground
    val borderColor = if (isCurrentHour) Color.White.copy(alpha = 0.4f) else HourlyCardBorder

    Box(modifier = Modifier.width(defaultCardWidth.dp)) {

        if (isCurrentHour) {
            Box( // halo glow
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .blur(30.dp, BlurredEdgeTreatment.Unbounded)
                    .clip(RoundedCornerShape(25.dp))
                    .background(HourlyCardSelected.copy(alpha = 0.5f))
            )
        }

        Column(
            modifier = modifier
                .width(defaultCardWidth.dp)
                .clip(RoundedCornerShape(25.dp))
                .background(backgroundColor)
                .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(25.dp))
                .padding(horizontal = 12.dp, vertical = 10.dp),
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
                modifier = Modifier.size(32.dp)
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = forecast.time.format(DateTimeFormatter.ofPattern("HH:mm")),
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 12.sp
            )
        }
    }

}
