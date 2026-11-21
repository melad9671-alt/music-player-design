package com.sonora.musicdesign.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Night = darkColorScheme(
    primary = Color(0xFFB9F36A),
    onPrimary = Color(0xFF17210A),
    secondary = Color(0xFFB9C9A1),
    background = Color(0xFF0B0D0B),
    surface = Color(0xFF151915),
    surfaceVariant = Color(0xFF222820),
    onBackground = Color(0xFFF4F6F0),
    onSurface = Color(0xFFF4F6F0),
    onSurfaceVariant = Color(0xFFA4ADA0)
)
private val Day = lightColorScheme(
    primary = Color(0xFF527D20),
    onPrimary = Color.White,
    secondary = Color(0xFF52634A),
    background = Color(0xFFF5F7F1),
    surface = Color.White,
    surfaceVariant = Color(0xFFE9EEE2),
    onBackground = Color(0xFF171B14),
    onSurface = Color(0xFF171B14),
    onSurfaceVariant = Color(0xFF626B5C)
)

@Composable
fun SonoraTheme(darkTheme: Boolean, content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (darkTheme) Night else Day,
        typography = androidx.compose.material3.Typography(),
        content = content
    )
}
