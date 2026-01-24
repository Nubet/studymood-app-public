package pl.nubet.studymood.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import kotlin.math.abs

object EmotionColors {

    fun forQuadrant(quadrant: Int): Color =
        when (quadrant) {
            1 -> GridQuadrantColors.HEP
            2 -> GridQuadrantColors.HEU
            3 -> GridQuadrantColors.LEU
            4 -> GridQuadrantColors.LEP
            else -> GridQuadrantColors.HEP
        }

    fun withVariation(baseColor: Color, seed: Int): Color {
        val hash = abs(seed)
        val hueShift = ((hash % 20) - 10) / 100f
        val satShift = ((hash % 10) - 5) / 100f

        val lighter = lightenColor(baseColor, 0.1f)
        val darker = darkenColor(baseColor, 0.1f)

        val intermediate = lerp(darker, lighter, 0.5f + hueShift)

        return lerp(intermediate, baseColor, 0.7f + satShift)
    }

    fun lightenColor(color: Color, factor: Float): Color =
        lerp(color, Color.White, factor.coerceIn(0f, 1f))

    fun darkenColor(color: Color, factor: Float): Color =
        lerp(color, Color.Black, factor.coerceIn(0f, 1f))
}
