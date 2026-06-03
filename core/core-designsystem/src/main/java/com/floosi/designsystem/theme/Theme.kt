package com.floosi.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun FloosiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) {
        FloosiDarkColorScheme.scheme
    } else {
        FloosiLightColorScheme.scheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = FloosiTypography.typography,
        content = content,
    )
}
