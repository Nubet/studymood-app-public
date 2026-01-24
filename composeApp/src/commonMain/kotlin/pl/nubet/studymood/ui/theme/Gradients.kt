package pl.nubet.studymood.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush

data class AppGradients(val primary45: Brush, val activeMood: Brush)

private val lightPrimary45: Brush =
    Brush.linearGradient(
        colors = listOf(GenZColors.Amber300, GenZColors.Amber500),
        start = Offset.Zero,
        end = Offset(1000f, 1000f),
    )

private val darkPrimary45: Brush =
    Brush.linearGradient(
        colors = listOf(GenZColors.Amber300, GenZColors.Amber700),
        start = Offset.Zero,
        end = Offset(1000f, 1000f),
    )

private val lightActiveMood: Brush =
    Brush.linearGradient(
        colors = listOf(GenZColors.Amber100, GenZColors.Olive300),
        start = Offset.Zero,
        end = Offset(0f, 1000f),
    )

private val darkActiveMood: Brush =
    Brush.linearGradient(
        colors = listOf(GenZColors.Olive300, GenZColors.Coffee700),
        start = Offset.Zero,
        end = Offset(0f, 1000f),
    )

fun lightGradients(): AppGradients =
    AppGradients(primary45 = lightPrimary45, activeMood = lightActiveMood)

fun darkGradients(): AppGradients =
    AppGradients(primary45 = darkPrimary45, activeMood = darkActiveMood)

val LocalAppGradients = staticCompositionLocalOf { lightGradients() }

object GenZGradients {
    @Composable fun primary45(): Brush = LocalAppGradients.current.primary45

    @Composable fun activeMood(): Brush = LocalAppGradients.current.activeMood
}
