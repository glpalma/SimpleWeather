package com.glpalma.simpleweather.ui.weather.components

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.glpalma.simpleweather.ui.theme.WeatherCardBottom
import com.glpalma.simpleweather.ui.theme.WeatherCardTop
import com.glpalma.simpleweather.ui.weather.WeatherScreenMode

private const val ANIM_DURATION = 400
private const val HALO_BOUNCE_DURATION = 600

val overshootEasing = Easing { fraction ->
    // Custom math for an overshoot effect
    val tension = 1.5f
    (fraction * fraction) * ((tension + 1) * fraction - tension)
}

@Composable
fun WeatherCard(
    mode: WeatherScreenMode, modifier: Modifier = Modifier, content: @Composable () -> Unit
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    val targetHeightFraction = when (mode) {
        WeatherScreenMode.TODAY -> 0.75f
        WeatherScreenMode.SEVEN_DAY -> 0.35f
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

    val lipOffset by animateDpAsState(
        targetValue = if (mode == WeatherScreenMode.CITY_PICKER) 0.dp else 8.dp,
        animationSpec = tween(durationMillis = ANIM_DURATION, easing = FastOutSlowInEasing),
        label = "lipOffset"
    )

    val haloOffset by animateDpAsState(
        targetValue = when (mode) {
            WeatherScreenMode.CITY_PICKER -> 0.dp
            WeatherScreenMode.TODAY -> 40.dp
            else -> 30.dp
        },
        animationSpec = tween(durationMillis = HALO_BOUNCE_DURATION, easing = overshootEasing),
        label = "haloOffset"
    )

    val cardHeight = screenHeight * heightFraction

    Box(modifier = modifier.fillMaxWidth().height(cardHeight)) {
        Box( // halo glow
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(120.dp)
                .align(Alignment.BottomCenter)
                .offset(y = haloOffset)
                .blur(70.dp, BlurredEdgeTreatment.Unbounded)
                .clip(CircleShape)
                .background(WeatherCardTop.copy(alpha = 0.2f))
        )

        Box( // lip
            modifier = Modifier
                .fillMaxWidth(0.975f)
                .height(60.dp)
                .align(Alignment.BottomCenter)
                .offset(y = lipOffset)
                .clip(RoundedCornerShape(bottomStart = cornerRadius, bottomEnd = cornerRadius))
                .border(1.dp, Color.Black.copy(alpha = 0.4f), shape = RoundedCornerShape(bottomStart = cornerRadius, bottomEnd = cornerRadius))
                .background(WeatherCardBottom.copy(alpha = 0.6f), )
        )

        Box( // main content
            modifier = Modifier
                .fillMaxWidth()
                .height(cardHeight)
                .clip(RoundedCornerShape(bottomStart = cornerRadius, bottomEnd = cornerRadius))
                .border(
                    1.dp,
                    Color.White.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(bottomStart = cornerRadius, bottomEnd = cornerRadius)
                )
                .background(
                    Brush.verticalGradient(
                        colors = listOf(WeatherCardTop, WeatherCardBottom)
                    )
                )
        ) {
            content()
        }


    }
}
