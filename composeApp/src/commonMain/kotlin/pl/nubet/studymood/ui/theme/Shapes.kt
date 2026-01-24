package pl.nubet.studymood.ui.theme

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

data class AppShapes(
    val card: CornerBasedShape = RoundedCornerShape(16.dp),
    val dialog: CornerBasedShape = RoundedCornerShape(24.dp),
    val chip: Shape = RoundedCornerShape(percent = 50),
    val button: CornerBasedShape = RoundedCornerShape(16.dp),
)

val LocalAppShapes = staticCompositionLocalOf { AppShapes() }
