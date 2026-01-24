package pl.nubet.studymood.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class AppElevations(
    val level1: Dp = 2.dp,
    val level2: Dp = 4.dp,
    val hoverAlpha: Float = 0.08f,
    val focusAlpha: Float = 0.12f,
)

val LocalAppElevations = staticCompositionLocalOf { AppElevations() }
