package pl.nubet.studymood.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Dimens(
    val x4: Dp = 4.dp,
    val x8: Dp = 8.dp,
    val x12: Dp = 12.dp,
    val x16: Dp = 16.dp,
    val x20: Dp = 20.dp,
    val x24: Dp = 24.dp,
)

val LocalDimens = staticCompositionLocalOf { Dimens() }
