package com.glpalma.simpleweather.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.RippleConfiguration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoundButton(onClick: () -> Unit, imageVector: ImageVector, contentDescription: String = "") {
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
        IconButton(
            modifier = Modifier.border(1.dp, Color.White.copy(alpha = 0.3f), CircleShape),
            onClick = onClick
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = contentDescription,
                tint = Color.White
            )
        }
    }
}