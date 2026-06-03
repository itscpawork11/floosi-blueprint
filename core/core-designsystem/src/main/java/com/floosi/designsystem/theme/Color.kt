package com.floosi.designsystem.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Brand colors
val FloosiGreen = Color(0xFF0F6E5C)
val FloosiGreenDark = Color(0xFF5DC4A8)
val FloosiGold = Color(0xFFD69E2E)
val FloosiGoldDark = Color(0xFFEBC07A)
val IncomeGreen = Color(0xFF1B7F3A)
val IncomeGreenDark = Color(0xFF7AD99A)
val ExpenseRed = Color(0xFFC8102E)
val ExpenseRedDark = Color(0xFFFF8A9C)
val TransferBlue = Color(0xFF1E88E5)
val TransferBlueDark = Color(0xFF82B1FF)

// Light theme colors
val LightPrimary = FloosiGreen
val LightOnPrimary = Color.White
val LightPrimaryContainer = Color(0xFFA8F2D9)
val LightOnPrimaryContainer = Color(0xFF002117)
val LightSecondary = Color(0xFF4A635F)
val LightTertiary = FloosiGold
val LightBackground = Color(0xFFFBF8F4)
val LightSurface = Color.White
val LightSurfaceVariant = Color(0xFFE8E5E0)
val LightSurfaceContainer = Color(0xFFF2EFEA)
val LightOnSurface = Color(0xFF1A1C1B)
val LightOnSurfaceVariant = Color(0xFF5A5C5A)
val LightOutline = Color(0xFFD4D7D4)

// Dark theme colors
val DarkPrimary = FloosiGreenDark
val DarkOnPrimary = Color(0xFF003828)
val DarkPrimaryContainer = Color(0xFF00513B)
val DarkOnPrimaryContainer = Color(0xFFA8F2D9)
val DarkSecondary = Color(0xFFB1CCC4)
val DarkTertiary = FloosiGoldDark
val DarkBackground = Color(0xFF101513)
val DarkSurface = Color(0xFF1A1F1D)
val DarkSurfaceVariant = Color(0xFF2A2F2D)
val DarkSurfaceContainer = Color(0xFF1F2422)
val DarkOnSurface = Color(0xFFE1E3E0)
val DarkOnSurfaceVariant = Color(0xFFC4C7C4)
val DarkOutline = Color(0xFF3A3D3B)

object FloosiLightColorScheme {
    val scheme = lightColorScheme(
        primary = LightPrimary,
        onPrimary = LightOnPrimary,
        primaryContainer = LightPrimaryContainer,
        onPrimaryContainer = LightOnPrimaryContainer,
        secondary = LightSecondary,
        tertiary = LightTertiary,
        background = LightBackground,
        surface = LightSurface,
        surfaceVariant = LightSurfaceVariant,
        onSurface = LightOnSurface,
        onSurfaceVariant = LightOnSurfaceVariant,
        outline = LightOutline,
    )
}

object FloosiDarkColorScheme {
    val scheme = darkColorScheme(
        primary = DarkPrimary,
        onPrimary = DarkOnPrimary,
        primaryContainer = DarkPrimaryContainer,
        onPrimaryContainer = DarkOnPrimaryContainer,
        secondary = DarkSecondary,
        tertiary = DarkTertiary,
        background = DarkBackground,
        surface = DarkSurface,
        surfaceVariant = DarkSurfaceVariant,
        onSurface = DarkOnSurface,
        onSurfaceVariant = DarkOnSurfaceVariant,
        outline = DarkOutline,
    )
}
