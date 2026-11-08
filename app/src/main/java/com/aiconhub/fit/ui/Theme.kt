package com.aiconhub.fit.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.aiconhub.fit.R

val Orange = Color(0xFFFF6B2C)
val OrangeBright = Color(0xFFFF8A50)
val Bg = Color(0xFF12100E)
val Surface = Color(0xFF1E1A17)
val Surface2 = Color(0xFF2A2420)
val OnBg = Color(0xFFF6F1EA)
val OnBgDim = Color(0xFFB9AFA4)
val Good = Color(0xFF66BB6A)

val Comfortaa = FontFamily(
    Font(R.font.comfortaa, FontWeight.Normal),
    Font(R.font.comfortaa, FontWeight.Bold)
)

private val DefaultScheme = darkColorScheme(
    primary = Orange,
    onPrimary = Color(0xFF1A0E06),
    primaryContainer = Color(0xFF3A2115),
    onPrimaryContainer = Color(0xFFFFD9C2),
    secondary = OrangeBright,
    onSecondary = Color(0xFF1A0E06),
    secondaryContainer = Color(0xFF3A2115),
    onSecondaryContainer = Color(0xFFFFD9C2),
    background = Bg,
    onBackground = OnBg,
    surface = Surface,
    onSurface = OnBg,
    surfaceVariant = Surface2,
    onSurfaceVariant = OnBgDim,
    tertiary = Good,
    outline = Color(0xFF4A4038)
)

private val HighContrastScheme = darkColorScheme(
    primary = Color(0xFFFF9E66),
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF4A2A16),
    onPrimaryContainer = Color.White,
    secondary = Color(0xFFFF9E66),
    onSecondary = Color.Black,
    background = Color.Black,
    onBackground = Color.White,
    surface = Color.Black,
    onSurface = Color.White,
    surfaceVariant = Color(0xFF1A1A1A),
    onSurfaceVariant = Color(0xFFE0E0E0),
    tertiary = Color(0xFF8BE08B),
    outline = Color(0xFF9A9A9A)
)

@Composable
fun FitTheme(highContrast: Boolean, content: @Composable () -> Unit) {
    val base = MaterialTheme.typography
    val typo = Typography(
        displaySmall = base.displaySmall.copy(fontFamily = Comfortaa, fontWeight = FontWeight.Bold),
        headlineLarge = base.headlineLarge.copy(fontFamily = Comfortaa, fontWeight = FontWeight.Bold),
        headlineMedium = base.headlineMedium.copy(fontFamily = Comfortaa, fontWeight = FontWeight.Bold),
        headlineSmall = base.headlineSmall.copy(fontFamily = Comfortaa, fontWeight = FontWeight.Bold),
        titleLarge = base.titleLarge.copy(fontFamily = Comfortaa, fontWeight = FontWeight.Bold),
        titleMedium = base.titleMedium.copy(fontFamily = Comfortaa, fontWeight = FontWeight.Bold)
    )
    MaterialTheme(
        colorScheme = if (highContrast) HighContrastScheme else DefaultScheme,
        typography = typo,
        content = content
    )
}
