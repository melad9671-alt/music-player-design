package com.sonora.musicdesign.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Night = darkColorScheme(
    primary = Color(0xFFB64BFF), onPrimary = Color.White,
    secondary = Color(0xFFE36BFF), background = Color(0xFF080611),
    surface = Color(0xFF171326), surfaceVariant = Color(0xFF211934),
    onBackground = Color(0xFFF8F3FF), onSurface = Color(0xFFF8F3FF),
    onSurfaceVariant = Color(0xFFAAA2C0)
)
private val Day = lightColorScheme(
    primary = Color(0xFF8D2CE0), onPrimary = Color.White,
    secondary = Color(0xFFB63DEB), background = Color(0xFFF7F2FF),
    surface = Color.White, surfaceVariant = Color(0xFFECE2F7),
    onBackground = Color(0xFF21152F), onSurface = Color(0xFF21152F),
    onSurfaceVariant = Color(0xFF70647D)
)

@Composable
fun SonoraTheme(darkTheme: Boolean, content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = if (darkTheme) Night else Day, typography = androidx.compose.material3.Typography(), content = content)
}
