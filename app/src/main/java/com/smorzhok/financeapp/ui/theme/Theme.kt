package com.smorzhok.financeapp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = GreenDark,
    secondary = SecondaryColorDark,
    tertiary = TertiaryColorDark,

    surface = SurfaceColorDark,
    surfaceContainer = SurfaceContainerColorDark,
    onSurface = OnSurfaceColorDark,
    outlineVariant = OutlineVariantColorDark,
    outline = OutlineColorDark,
    surfaceVariant = SurfaceVariantDarkColor,
    surfaceContainerHigh = SurfaceContainerHighDarkColor,
    error = ErrorColorDark
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = SecondaryColorLight,
    tertiary = TertiaryColorLight,

    surface = SurfaceColorLight,
    surfaceContainer = SurfaceContainerColorLight,
    onSurface = OnSurfaceColorLight,
    outlineVariant = OutlineVariantColorLight,
    outline = OutlineColorLight,
    surfaceVariant = SurfaceVariantLightColor,
    surfaceContainerHigh = SurfaceContainerHighLightColor,
    error = ErrorColor
)

@Composable
fun FinanceAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}