package com.glpalma.simpleweather.ui.weather.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.glpalma.simpleweather.domain.model.WeatherReport
import com.glpalma.simpleweather.ui.theme.Rubik
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun TodayCardContent(
    currentWeather: WeatherReport?, isRefreshing: Boolean, modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(0.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        AnimatedVisibility(
            visible = isRefreshing, enter = fadeIn(), exit = fadeOut()
        ) {
            UpdatingBadge()
        }

        Spacer(Modifier.weight(1f))

        if (currentWeather != null) {
            val current = currentWeather.current

            WeatherIcon(
                condition = current.condition,
                isDay = current.isDay,
                modifier = Modifier.size(120.dp)
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "${current.temperatureC.toInt()}º",
                fontSize = 110.sp,
                color = Color.White,
                fontFamily = Rubik,
            )

            Text(
                text = current.condition.displayName,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = LocalDate.now().format(
                    DateTimeFormatter.ofPattern("EEEE, d MMMM", Locale.getDefault())
                ), fontSize = 14.sp, color = Color.White.copy(alpha = 0.7f)
            )
        } else {
            CircularProgressIndicator(color = Color.White)
        }

        Spacer(Modifier.weight(1f))
    }
}

@Composable
fun UpdatingBadge(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        color = Color(0xFF2E7D32).copy(alpha = 0.4f),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(Color(0xFF4CAF50), CircleShape)
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = "Updating",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
