package pl.nubet.studymood.ui.screens.breathing.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun BreathingRing(progress: Float, modifier: Modifier = Modifier) {
    val backgroundColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.08f)
    val progressColor = MaterialTheme.colorScheme.primary

    Canvas(modifier = modifier.aspectRatio(1f)) {
        val strokeWidth = 4.dp.toPx()
        val radius = (size.minDimension / 2) - strokeWidth
        val center = Offset(size.width / 2, size.height / 2)

        drawCircle(
            color = backgroundColor,
            radius = radius,
            center = center,
            style = Stroke(width = 3.dp.toPx()),
        )

        drawArc(
            color = progressColor,
            startAngle = -90f,
            sweepAngle = 360f * progress,
            useCenter = false,
            topLeft = Offset(center.x - radius, center.y - radius),
            size = Size(radius * 2, radius * 2),
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
        )
    }
}
