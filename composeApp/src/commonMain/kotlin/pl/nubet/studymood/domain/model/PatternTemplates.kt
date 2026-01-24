package pl.nubet.studymood.domain.model

import kotlin.math.*


object PatternTemplates {
    private val wavePattern =
        PatternTemplate(id = "wave", label = "Wave line", microcopy = "A gentle wave to trace.") {
            width,
            height ->
            val marginX = 0.15f
            val centerY = 0.5f
            val count = 7
            val amplitude = 0.08f

            List(count) { i ->
                val t = i.toFloat() / (count - 1)
                val x = marginX + t * (1f - 2 * marginX)
                val y = centerY + sin(t * PI.toFloat()) * amplitude
                NormalizedPoint(x, y)
            }
        }

    private val stepsPattern =
        PatternTemplate(
            id = "steps",
            label = "Steps",
            microcopy = "A small staircase of points.",
        ) { width, height ->
            val marginX = 0.15f
            val baseY = 0.65f
            val stepHeight = 0.08f
            val horizontalStep = (1f - 2 * marginX) / 4f

            buildList {
                var x = marginX
                var y = baseY
                add(NormalizedPoint(x, y))

                repeat(3) {
                    x += horizontalStep
                    add(NormalizedPoint(x, y))
                    y -= stepHeight
                    add(NormalizedPoint(x, y))
                }
            }
        }

    private val arcPattern =
        PatternTemplate(id = "arc", label = "Arc", microcopy = "A soft arc across the canvas.") {
            width,
            height ->
            val marginX = 0.15f
            val centerX = 0.5f
            val topY = 0.35f
            val bottomY = 0.65f
            val segments = 6

            List(segments + 1) { i ->
                val t = i.toFloat() / segments
                val angle = PI.toFloat() + t * PI.toFloat()
                val radius = 0.15f
                val x = centerX + radius * cos(angle)
                val y = (topY + bottomY) / 2f + radius * sin(angle)
                NormalizedPoint(x, y)
            }
        }

    private val zigzagPattern =
        PatternTemplate(id = "zigzag", label = "Zigzag", microcopy = "A simple zigzag path.") {
            width,
            height ->
            val marginX = 0.15f
            val centerY = 0.5f
            val amplitude = 0.08f
            val count = 7

            List(count) { i ->
                val t = i.toFloat() / (count - 1)
                val x = marginX + t * (1f - 2 * marginX)
                val y = centerY + (if (i % 2 == 0) -amplitude else amplitude)
                NormalizedPoint(x, y)
            }
        }

    val templates = listOf(wavePattern, stepsPattern, arcPattern, zigzagPattern)

    fun selectRandom(): PatternTemplate = templates.random()
}
