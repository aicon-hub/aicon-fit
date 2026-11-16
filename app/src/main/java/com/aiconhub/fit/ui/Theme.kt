package com.aiconhub.fit.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.aiconhub.fit.R

val Orange = Color(0xFFFF6B2C)
val OrangeSoft = Color(0xFFFF9A5C)
val OrangeDeep = Color(0xFFE04E0B)
val Mint = Color(0xFF7BD88A)

val Bg = Color(0xFF0F0C09)
val Surface = Color(0xFF1B1611)
val Surface2 = Color(0xFF29211A)
val OnBg = Color(0xFFF8F2E9)
val OnBgDim = Color(0xFFADA093)

val Comfortaa = FontFamily(
    Font(R.font.comfortaa, FontWeight.Normal),
    Font(R.font.comfortaa, FontWeight.Bold)
)

/** The brand ember gradient used for hero cards and primary actions. */
fun emberBrush(): Brush = Brush.linearGradient(listOf(OrangeSoft, Orange, OrangeDeep))

private val DarkScheme = darkColorScheme(
    primary = Orange,
    onPrimary = Color(0xFF1A0E06),
    primaryContainer = Color(0xFF3A2115),
    onPrimaryContainer = Color(0xFFFFD9C2),
    secondary = OrangeSoft,
    onSecondary = Color(0xFF1A0E06),
    secondaryContainer = Color(0xFF3A2115),
    onSecondaryContainer = Color(0xFFFFD9C2),
    background = Bg,
    onBackground = OnBg,
    surface = Surface,
    onSurface = OnBg,
    surfaceVariant = Surface2,
    onSurfaceVariant = OnBgDim,
    tertiary = Mint,
    outline = Color(0xFF4A4038)
)

private val BlackScheme = darkColorScheme(
    primary = Orange,
    onPrimary = Color(0xFF1A0E06),
    primaryContainer = Color(0xFF331C10),
    onPrimaryContainer = Color(0xFFFFD9C2),
    secondary = OrangeSoft,
    onSecondary = Color(0xFF1A0E06),
    background = Color.Black,
    onBackground = OnBg,
    surface = Color(0xFF101010),
    onSurface = OnBg,
    surfaceVariant = Color(0xFF1F1B18),
    onSurfaceVariant = OnBgDim,
    tertiary = Mint,
    outline = Color(0xFF3D362F)
)

private val LightScheme = lightColorScheme(
    primary = Color(0xFFE2570F),
    onPrimary = Color(0xFFFFF6EE),
    primaryContainer = Color(0xFFFFDCC4),
    onPrimaryContainer = Color(0xFF5A2000),
    secondary = Color(0xFFB7490E),
    onSecondary = Color(0xFFFFF6EE),
    secondaryContainer = Color(0xFFFFDCC4),
    onSecondaryContainer = Color(0xFF5A2000),
    background = Color(0xFFFAF5EE),
    onBackground = Color(0xFF261B10),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF261B10),
    surfaceVariant = Color(0xFFF1E8DC),
    onSurfaceVariant = Color(0xFF6F6152),
    tertiary = Color(0xFF2E7D46),
    outline = Color(0xFFC9BBA9)
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
fun FitTheme(theme: String = "dark", highContrast: Boolean = false, content: @Composable () -> Unit) {
    val base = MaterialTheme.typography
    val typo = Typography(
        displayMedium = base.displayMedium.copy(fontFamily = Comfortaa, fontWeight = FontWeight.Bold),
        displaySmall = base.displaySmall.copy(fontFamily = Comfortaa, fontWeight = FontWeight.Bold),
        headlineLarge = base.headlineLarge.copy(fontFamily = Comfortaa, fontWeight = FontWeight.Bold),
        headlineMedium = base.headlineMedium.copy(fontFamily = Comfortaa, fontWeight = FontWeight.Bold),
        headlineSmall = base.headlineSmall.copy(fontFamily = Comfortaa, fontWeight = FontWeight.Bold),
        titleLarge = base.titleLarge.copy(fontFamily = Comfortaa, fontWeight = FontWeight.Bold),
        titleMedium = base.titleMedium.copy(fontFamily = Comfortaa, fontWeight = FontWeight.Bold)
    )
    val scheme = if (highContrast) HighContrastScheme else when (theme) {
        "black" -> BlackScheme
        "light" -> LightScheme
        else -> DarkScheme
    }
    MaterialTheme(
        colorScheme = scheme,
        typography = typo,
        content = content
    )
}
