package com.unilibre.finanzasapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary        = Verde500,
    onPrimary      = androidx.compose.ui.graphics.Color.White,
    primaryContainer = Verde50,
    secondary      = Azul700,
    error          = Rojo500,
    errorContainer = Rojo50,
    background     = Gris100,
    surface        = androidx.compose.ui.graphics.Color.White,
    onBackground   = Gris800,
    onSurface      = Gris800
)

private val DarkColors = darkColorScheme(
    primary        = Verde200,
    onPrimary      = VerdeOscuro,
    primaryContainer = VerdeOscuro,
    secondary      = Verde200,
    error          = Rojo200,
    background     = SuperficieOscura,
    surface        = Gris800,
    onBackground   = androidx.compose.ui.graphics.Color.White,
    onSurface      = androidx.compose.ui.graphics.Color.White
)

@Composable
fun FinanzasTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography  = FinanzasTypography,
        content     = content
    )
}