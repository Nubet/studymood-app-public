package pl.nubet.studymood.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class QuadrantColors(
    val overlayHighPleasantHighEnergy: Color = GridQuadrantColors.HEP,
    val overlayHighEnergyUnpleasant: Color = GridQuadrantColors.HEU,
    val overlayLowEnergyUnpleasant: Color = GridQuadrantColors.LEU,
    val overlayLowEnergyPleasant: Color = GridQuadrantColors.LEP,
    val axisColor: Color = GenZColors.Greige300,
    val axisLabelColor: Color = GenZColors.Greige500,
    val gridColor: Color = GenZColors.Greige300,
    val pointOuter: Color = GenZColors.Amber500,
    val pointInner: Color = Color.White,
    val pointGlow: Color = GenZColors.Amber300.copy(alpha = 0.30f),
    val pointShadow: Color = GenZColors.Amber900.copy(alpha = 0.12f),
)

val LocalQuadrantColors = staticCompositionLocalOf { QuadrantColors() }

fun lightQuadrantColors(): QuadrantColors =
    QuadrantColors(
        axisColor = GenZColors.Greige300.copy(alpha = 0.42f),
        axisLabelColor = GenZColors.Greige500,
        gridColor = GenZColors.Greige300.copy(alpha = 0.28f),
    )

fun darkQuadrantColors(scheme: ColorScheme): QuadrantColors =
    QuadrantColors(
        axisColor = GenZColors.Greige300.copy(alpha = 0.65f),
        axisLabelColor = GenZColors.Greige200,
        gridColor = GenZColors.Greige500.copy(alpha = 0.25f),
        pointOuter = scheme.primary,
        pointInner = scheme.surface,
        pointGlow = GenZColors.Amber400.copy(alpha = 0.35f),
        pointShadow = GenZColors.Coffee900.copy(alpha = 0.40f),
    )
