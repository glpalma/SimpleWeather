package com.glpalma.simpleweather.ui.weather.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.glpalma.simpleweather.ui.theme.WeatherCardBottom
import com.glpalma.simpleweather.ui.theme.WeatherCardTop
import com.glpalma.simpleweather.ui.weather.WeatherScreenMode

private const val ANIM_DURATION = 400

@Composable
fun WeatherCard(
    mode: WeatherScreenMode,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    val targetHeightFraction = when (mode) {
        WeatherScreenMode.TODAY -> 0.65f
        WeatherScreenMode.SEVEN_DAY -> 0.28f
        WeatherScreenMode.CITY_PICKER -> 1f
    }

    val heightFraction by animateFloatAsState(
        targetValue = targetHeightFraction,
        animationSpec = tween(durationMillis = ANIM_DURATION, easing = FastOutSlowInEasing),
        label = "cardHeight"
    )

    val cornerRadius by animateDpAsState(
        targetValue = if (mode == WeatherScreenMode.CITY_PICKER) 0.dp else 50.dp,
        animationSpec = tween(durationMillis = ANIM_DURATION, easing = FastOutSlowInEasing),
        label = "cornerRadius"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(screenHeight * heightFraction)
            .clip(RoundedCornerShape(bottomStart = cornerRadius, bottomEnd = cornerRadius))
            .background(
                Brush.verticalGradient(
                    colors = listOf(WeatherCardTop, WeatherCardBottom)
                )
            )
    ) {
        content()
    }
}
