package pl.nubet.studymood.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

data class AppTypography(
    val h1: TextStyle = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Bold),
    val h2: TextStyle = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.SemiBold),
    val h3: TextStyle = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.SemiBold),
    val body1: TextStyle = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium),
    val body2: TextStyle = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Normal),
    val caption: TextStyle = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Normal),
) {
    val title: TextStyle
        get() = h1

    val section: TextStyle
        get() = h2

    val body: TextStyle
        get() = body1
}

val LocalAppTypography = staticCompositionLocalOf { AppTypography() }
