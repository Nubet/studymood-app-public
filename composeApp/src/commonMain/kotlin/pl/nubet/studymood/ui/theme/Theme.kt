package pl.nubet.studymood.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun AppTheme(dark: Boolean = false, content: @Composable () -> Unit) {
    val scheme = if (dark) darkScheme else lightScheme
    val quadColors = if (dark) darkQuadrantColors(scheme) else lightQuadrantColors()
    val gradients = if (dark) darkGradients() else lightGradients()

    CompositionLocalProvider(
        LocalQuadrantColors provides quadColors,
        LocalAppTypography provides AppTypography(),
        LocalDimens provides Dimens(),
        LocalAppShapes provides AppShapes(),
        LocalAppElevations provides AppElevations(),
        LocalAppGradients provides gradients,
    ) {
        MaterialTheme(colorScheme = scheme, content = content)
    }
}
