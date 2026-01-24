package pl.nubet.studymood.ui.screens.disappearingcenter.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import pl.nubet.studymood.domain.model.DrawingStroke
import pl.nubet.studymood.domain.model.NormalizedPoint

@Composable
fun CenterCanvas(
    centerPoint: NormalizedPoint,
    centerAlpha: Float,
    centerRadius: Float,
    strokes: List<DrawingStroke>,
    currentStroke: List<NormalizedPoint>,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        if (centerAlpha > 0f) {
            drawCenterPoint(
                centerPoint = centerPoint,
                alpha = centerAlpha,
                radius = centerRadius,
                width = width,
                height = height,
            )
        }

        strokes.forEach { stroke -> drawStroke(stroke.points, width, height) }

        if (currentStroke.isNotEmpty()) {
            drawStroke(currentStroke, width, height)
        }
    }
}

private fun DrawScope.drawCenterPoint(
    centerPoint: NormalizedPoint,
    alpha: Float,
    radius: Float,
    width: Float,
    height: Float,
) {
    val centerX = centerPoint.x * width
    val centerY = centerPoint.y * height

    drawCircle(
        color = Color(0xFFFFAB17).copy(alpha = alpha * 0.3f),
        radius = radius * 2.5f,
        center = Offset(centerX, centerY),
    )

    drawCircle(
        color = Color(0xFFFFAB17).copy(alpha = alpha * 0.5f),
        radius = radius * 1.5f,
        center = Offset(centerX, centerY),
    )

    drawCircle(
        color = Color(0xFFFFAB17).copy(alpha = alpha * 0.85f),
        radius = radius,
        center = Offset(centerX, centerY),
    )

    drawCircle(
        color = Color(0xFF432D11).copy(alpha = alpha * 0.4f),
        radius = radius,
        center = Offset(centerX, centerY),
        style = Stroke(width = 1.5f),
    )
}

private fun DrawScope.drawStroke(points: List<NormalizedPoint>, width: Float, height: Float) {
    if (points.size < 2) return

    val path = androidx.compose.ui.graphics.Path()

    val first = points.first()
    path.moveTo(first.x * width, first.y * height)

    for (i in 1 until points.size) {
        val point = points[i]
        path.lineTo(point.x * width, point.y * height)
    }

    drawPath(
        path = path,
        color = Color(0xFF7A4130).copy(alpha = 0.86f),
        style = Stroke(width = 3f, cap = StrokeCap.Round, join = StrokeJoin.Round),
    )
}
