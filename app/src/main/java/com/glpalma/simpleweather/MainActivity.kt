package com.glpalma.simpleweather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.glpalma.simpleweather.ui.theme.SimpleWeatherTheme
import com.glpalma.simpleweather.ui.weather.WeatherRoute
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimpleWeatherTheme(
                darkTheme = true,
                dynamicColor = false
            ) {
                WeatherRoute()
            }
        }
    }
}
