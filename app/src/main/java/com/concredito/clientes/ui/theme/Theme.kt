package com.concredito.clientes.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorScheme = darkColorScheme(
    primary = finaCreditoThemeDarkPrimary,
    onPrimary = finaCreditoThemeDarkOnPrimary,
    primaryContainer = finaCreditoThemeDarkPrimaryContainer,
    onPrimaryContainer = finaCreditoThemeDarkOnPrimaryContainer,
    secondary = finaCreditoThemeDarkSecondary,
    onSecondary = finaCreditoThemeDarkOnSecondary,
    secondaryContainer = finaCreditoThemeDarkSecondaryContainer,
    onSecondaryContainer = finaCreditoThemeDarkOnSecondaryContainer,
    tertiary = finaCreditoThemeDarkTertiary,
    onTertiary = finaCreditoThemeDarkOnTertiary,
    tertiaryContainer = finaCreditoThemeDarkTertiaryContainer,
    onTertiaryContainer = finaCreditoThemeDarkOnTertiaryContainer,
    error = finaCreditoThemeDarkError,
    errorContainer = finaCreditoThemeDarkErrorContainer,
    onError = finaCreditoThemeDarkOnError,
    onErrorContainer = finaCreditoThemeDarkOnErrorContainer,
    background = finaCreditoThemeDarkBackground,
    onBackground = finaCreditoThemeDarkOnBackground,
    surface = finaCreditoThemeDarkSurface,
    onSurface = finaCreditoThemeDarkOnSurface,
    surfaceVariant = finaCreditoThemeDarkSurfaceVariant,
    onSurfaceVariant = finaCreditoThemeDarkOnSurfaceVariant,
    outline = finaCreditoThemeDarkOutline,
    inverseOnSurface = finaCreditoThemeDarkInverseOnSurface,
    inverseSurface = finaCreditoThemeDarkInverseSurface,
    inversePrimary = finaCreditoThemeDarkInversePrimary,
    surfaceTint = finaCreditoThemeDarkSurfaceTint,
    outlineVariant = finaCreditoThemeDarkOutlineVariant,
    scrim = finaCreditoThemeDarkScrim,

)

private val LightColorScheme = lightColorScheme(
    primary = finaCreditoThemeLightPrimary,
    onPrimary = finaCreditoThemeLightOnPrimary,
    primaryContainer = finaCreditoThemeLightPrimaryContainer,
    onPrimaryContainer = finaCreditoThemeLightOnPrimaryContainer,
    secondary = finaCreditoThemeLightSecondary,
    onSecondary = finaCreditoThemeLightOnSecondary,
    secondaryContainer = finaCreditoThemeLightSecondaryContainer,
    onSecondaryContainer = finaCreditoThemeLightOnSecondaryContainer,
    tertiary = finaCreditoThemeLightTertiary,
    onTertiary = finaCreditoThemeLightOnTertiary,
    tertiaryContainer = finaCreditoThemeLightTertiaryContainer,
    onTertiaryContainer = finaCreditoThemeLightOnTertiaryContainer,
    error = finaCreditoThemeLightError,
    errorContainer = finaCreditoThemeLightErrorContainer,
    onError = finaCreditoThemeLightOnError,
    onErrorContainer = finaCreditoThemeLightOnErrorContainer,
    background = finaCreditoThemeLightBackground,
    onBackground = finaCreditoThemeLightOnBackground,
    surface = finaCreditoThemeLightSurface,
    onSurface = finaCreditoThemeLightOnSurface,
    surfaceVariant = finaCreditoThemeLightSurfaceVariant,
    onSurfaceVariant = finaCreditoThemeLightOnSurfaceVariant,
    outline = finaCreditoThemeLightOutline,
    inverseOnSurface = finaCreditoThemeLightInverseOnSurface,
    inverseSurface = finaCreditoThemeLightInverseSurface,
    inversePrimary = finaCreditoThemeLightInversePrimary,
    surfaceTint = finaCreditoThemeLightSurfaceTint,
    outlineVariant = finaCreditoThemeLightOutlineVariant,
    scrim = finaCreditoThemeLightScrim,
)

@Composable
fun ClientesProspectosTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    val colors = if (!darkTheme) {
        LightColorScheme
    } else {
        DarkColorScheme
    }

    val systemUiController = rememberSystemUiController()

    // Configurar el color de la barra de estado según el tema claro/oscuro
    SideEffect {
        systemUiController.setSystemBarsColor(
            color = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) colorScheme.primary else colors.primary,
            darkIcons = darkTheme,
        )
    }
    MaterialTheme(
        colorScheme = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) colorScheme else colors,
        typography = Typography,
        content = content,
    )
}
